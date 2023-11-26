package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.utils.TestConstants.exception
import com.example.orderingapp.main.data.utils.TestConstants.msgEx
import com.example.orderingapp.main.domain.model.Item
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

    @Before
    fun setup() {
        getItemsUseCase = GetItemsRepository(firestore, dao)

        every { firestore.collection("items") } returns collection

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
                    operation = if (isRemoving) 2 else 3
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
        every { dao.getItems() } returns listOf(itemDTO)
        getItemsUseCase.getItemsFromLocal().collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun testExceptionGetItemsLocal() = runTest {
        every { dao.getItems() } throws exception
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
        getItemsUseCase.getItemsFromRemote().take(1)
        every { document.id } returns itemModify.id
        every { document["description"] } returns itemModify.description
        every { document["currentValue"] } returns itemModify.currentValue
        every { document["minimumStock"] } returns itemModify.minimumStock
        every { document["currentStock"] } returns itemModify.currentStock
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).contains(itemModify)
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
            assertThat(result.data).doesNotContain(itemModify)
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
        every { firestoreException.message } returns msgEx
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertError(result)
        }
    }

    @Test
    fun testRuntimeException() = runTest {
        every { documentChange.type } throws exception
        getItemsUseCase.getItemsFromRemote().take(1).collect { result ->
            assertError(result)
        }
    }

    private fun assertSuccess(result: ApiResult<List<Item>>) {
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        result as ApiResult.Success
        assertThat(result.data).contains(item)
    }

    private fun assertError(
        result: ApiResult<List<Item>>,
    ) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).isEqualTo(msgEx)
    }

    companion object {
        val item = Item(
            id = "123",
            description = "testeItem",
            currentValue = 10.0,
            minimumStock = 5,
            currentStock = 10,
            quantity = 0
        )
        val itemModify = Item(
            id = item.id,
            description = "${item.description} modificado",
            currentValue = item.currentValue,
            minimumStock = item.minimumStock,
            currentStock = item.currentStock,
            quantity = 0
        )
        val itemDTO = ItemDTO(
            id = "123",
            description = "testeItem",
            currentValue = 10.0,
            minimumStock = 5,
            currentStock = 10,
        )
    }
}