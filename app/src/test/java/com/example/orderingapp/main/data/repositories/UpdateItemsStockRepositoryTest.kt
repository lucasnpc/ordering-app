package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.domain.usecase.UpdateItemsStockUseCase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CompletableFuture


class UpdateItemsStockRepositoryTest {

    private lateinit var updateItemsStockUseCase: UpdateItemsStockUseCase
    private val firestore = mockk<FirebaseFirestore>()
    private val documentReference: DocumentReference = mockk()
    private val listItems = TestData().itemsCompose

    @Before
    fun setUp() {
        updateItemsStockUseCase = UpdateItemsStockRepository(firestore)
    }

    @Test
    fun updateItemsStock() = runTest {
        val completableFuture = CompletableFuture<Unit>()
        every { firestore.collection(FirestoreCollections.items) } returns mockk {
            every { document(any()) } returns documentReference
        }
        every { documentReference.set(any()) } answers {
            val taskCompletionSource = TaskCompletionSource<Void>()
            taskCompletionSource.setResult(null)  // Mark task as successful
            completableFuture.complete(Unit)
            val task = taskCompletionSource.task
            mockk<Task<Void>> {
                every { addOnCompleteListener(any()) } answers {
                    firstArg<OnCompleteListener<Void>>().onComplete(task)
                    task
                }
            }
        }
        updateItemsStockUseCase.updateItemsStock(listItems).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        }
        withContext(Dispatchers.IO) {
            completableFuture.get()
        }
    }

    @Test
    fun updateItemsStockException() = runTest {
        every { firestore.collection(FirestoreCollections.items) } returns mockk {
            every { document(any()) } throws testException
        }
        updateItemsStockUseCase.updateItemsStock(listItems).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(TestConstants.testMsgException)
        }
    }
}