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
    private val mapItem = TestData().itemsCompose.entries.first()
    private val itemModified = ItemCompose(
        Item(
            description = "${mapItem.value.item.description} modificado",
            currentValue = mapItem.value.item.currentValue,
            minimumStock = mapItem.value.item.minimumStock,
            currentStock = mapItem.value.item.currentStock,
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

        every { document.id } returns mapItem.key
        every { document["description"] } returns mapItem.value.item.description
        every { document["currentValue"] } returns mapItem.value.item.currentValue
        every { document["costValue"] } returns mapItem.value.item.costValue
        every { document["minimumStock"] } returns mapItem.value.item.minimumStock
        every { document["currentStock"] } returns mapItem.value.item.currentStock
    }

    @Test
    fun getItemsLocal() = runTest {
        every { dao.getItems() } returns listDTO.values.toList()
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
        every { document["description"] } returns itemModified.item.description
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data.values.first().item).isEqualTo(itemModified.item)
            assertThat(result.data.values.first().quantity.value).isEqualTo(itemModified.quantity.value)
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

    private fun assertSuccess(result: ApiResult<Map<String, ItemCompose>>) {
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        result as ApiResult.Success
        for ((key, value) in result.data) {
            val expected = list[key]?.item
            value.item.run {
                assertThat(description).isEqualTo(expected?.description)
                assertThat(currentValue).isEqualTo(expected?.currentValue)
                assertThat(currentStock).isEqualTo(expected?.currentStock)
                assertThat(minimumStock).isEqualTo(expected?.minimumStock)
                assertThat(finalQuantity).isEqualTo(0)
            }
        }
    }

    private fun assertError(
        result: ApiResult<Map<String, ItemCompose>>,
    ) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).isEqualTo(testMsgException)
    }
}