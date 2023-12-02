package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetItemsRepositoryTest {
    private lateinit var getItemsUseCase: GetItemsUseCase

    private val firestore: FirebaseFirestore = mockk()

    private val collection: CollectionReference = mockk()

    private val documentChange: DocumentChange = mockk()

    private val document: QueryDocumentSnapshot = mockk()

    private val snapshot: QuerySnapshot = mockk()

    private val dao: OrderingAppDao = mockk()

    // 1 to add, 2 to modify 3 to remove
    private var operation = 1
    private var isRemoving = false

    private val list = TestData().itemsCompose
    private val listDTO = TestData().itemsDTO
    private val item = TestData().itemsCompose.first().items
    private val itemModified = ItemCompose(
        Item(
            id = item.id,
            description = "${item.description} modificado",
            currentValue = item.currentValue,
            minimumStock = item.minimumStock,
            currentStock = item.currentStock,
            finalQuantity = 0
        )
    )

    @Before
    fun setup() {
        getItemsUseCase = GetItemsRepository(firestore, dao)

        every { firestore.collection(FirestoreCollections.items) } returns collection

        every { collection.addSnapshotListener(any()) } answers {
            val listener = firstArg<EventListener<QuerySnapshot>>()
            listener.onEvent(snapshot, null)
            mockk {
                every { remove() } just Runs
            }
        }

        every { snapshot.documentChanges } returns listOf(
            documentChange
        )
        every { documentChange.type } answers {
            when (operation) {
                1 -> {
                    operation = if (!isRemoving) 2 else 3
                    DocumentChange.Type.ADDED
                }
                2 -> DocumentChange.Type.MODIFIED
                else -> DocumentChange.Type.REMOVED
            }
        }

        every { documentChange.document } returns document

        every { document.id } returns item.id
        every { document["description"] } returns item.description
        every { document["currentValue"] } returns item.currentValue
        every { document["minimumStock"] } returns item.minimumStock
        every { document["currentStock"] } returns item.currentStock
    }

    @Test
    fun getItemsLocal() = runTest {
        every { dao.getItems() } returns listDTO
        getItemsUseCase.getItemsFromLocal().collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun testExceptionGetItemsLocal() = runTest {
        every { dao.getItems() } throws testException
        getItemsUseCase.getItemsFromLocal().collect { result ->
            assertError(result)
        }
    }

    @Test
    fun addItems() = runTest {
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun modifyItems() = runTest {
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertSuccess(result)
        }
        every { document["description"] } returns itemModified.items.description
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data.first().items).isEqualTo(itemModified.items)
            assertThat(result.data.first().quantity.value).isEqualTo(itemModified.quantity.value)
        }
    }

    @Test
    fun removeItems() = runTest {
        isRemoving = true
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertSuccess(result)
        }
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).isEmpty()
        }
    }

    @Test
    fun testException() = runTest {
        val firestoreException = mockk<FirebaseFirestoreException>()
        every { collection.addSnapshotListener(any()) } answers {
            val listener = firstArg<EventListener<QuerySnapshot>>()
            listener.onEvent(null, firestoreException)
            mockk {
                every { remove() } just Runs
            }
        }
        every { firestoreException.message } returns testMsgException
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertError(result)
        }
    }

    @Test
    fun testRuntimeException() = runTest {
        every { documentChange.type } throws testException
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertError(result)
        }
    }

    private fun assertSuccess(result: ApiResult<List<ItemCompose>>) {
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        result as ApiResult.Success
        for (i in result.data.indices) {
            result.data[i].items.run {
                assertThat(id).isEqualTo(list[i].items.id)
                assertThat(description).isEqualTo(list[i].items.description)
                assertThat(currentValue).isEqualTo(list[i].items.currentValue)
                assertThat(currentStock).isEqualTo(list[i].items.currentStock)
                assertThat(minimumStock).isEqualTo(list[i].items.minimumStock)
                assertThat(finalQuantity).isEqualTo(0)
            }
        }
    }

    private fun assertError(
        result: ApiResult<List<ItemCompose>>,
    ) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).isEqualTo(testMsgException)
    }
}