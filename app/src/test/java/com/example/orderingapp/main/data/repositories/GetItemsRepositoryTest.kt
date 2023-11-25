package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.entities.Item
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetItemsRepositoryTest {
    private lateinit var getItemsUseCase: GetItemsUseCase

    private val firestore: FirebaseFirestore = mockk()

    private val collection: CollectionReference = mockk(relaxed = true)

    private val documentChange: DocumentChange = mockk(relaxed = true)

    private val document: QueryDocumentSnapshot = mockk(relaxed = true)

    private val snapshot: QuerySnapshot = mockk(relaxed = true)

    // 1 to add, 2 to modify 3 to remove
    var operation = 1

    @Before
    fun setup() {
        getItemsUseCase = GetItemsRepository(firestore)

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
                    operation = 2
                    DocumentChange.Type.ADDED
                }
                2 -> {
                    operation = 3
                    DocumentChange.Type.MODIFIED
                }
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
    fun addItems() = runBlockingTest {
        getItemsUseCase.getItems().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).contains(item)
        }
    }

    @Test
    fun modifyItems() = runBlockingTest {
        getItemsUseCase.getItems().take(1)
        every { document.id } returns itemModify.id
        every { document["description"] } returns itemModify.description
        every { document["currentValue"] } returns itemModify.currentValue
        every { document["minimumStock"] } returns itemModify.minimumStock
        every { document["currentStock"] } returns itemModify.currentStock
        getItemsUseCase.getItems().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).contains(itemModify)
        }
    }

    @Test
    fun removeItems() = runBlockingTest {
        getItemsUseCase.getItems().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).contains(item)
        }
        getItemsUseCase.getItems().take(1)
        getItemsUseCase.getItems().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).doesNotContain(itemModify)
        }
    }

    @Test
    fun testException() = runBlockingTest {
        val exception = mockk<FirebaseFirestoreException>()
        val msgEx = "messageException"
        every { collection.addSnapshotListener(any()) } answers {
            val listener = firstArg<EventListener<QuerySnapshot>>()
            listener.onEvent(null, exception)
            mockk {
                every { remove() } just Runs
            }
        }
        every { exception.message } returns msgEx
        getItemsUseCase.getItems().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(msgEx)
        }
    }

    @Test
    fun testRuntimeException() = runBlockingTest {
        val msgEx = "messageException"
        val exception = RuntimeException(msgEx)
        every { documentChange.type } throws exception
        getItemsUseCase.getItems().take(1).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(msgEx)
        }
    }

    companion object {
        val item = Item(
            id = "123",
            description = "testeItem",
            currentValue = 10.0,
            minimumStock = 5,
            currentStock = 10
        )
        val itemModify = Item(
            id = item.id,
            description = "${item.description} modificado",
            currentValue = item.currentValue,
            minimumStock = item.minimumStock,
            currentStock = item.currentStock
        )
    }
}