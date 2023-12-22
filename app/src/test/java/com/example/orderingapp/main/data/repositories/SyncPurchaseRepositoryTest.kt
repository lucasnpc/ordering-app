package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.repositories.utils.FirestoreCollections
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.domain.usecase.SyncPurchaseUseCase
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

class SyncPurchaseRepositoryTest {

    private lateinit var syncPurchaseUseCase: SyncPurchaseUseCase

    private val firestore: FirebaseFirestore = mockk()
    private val documentReference: DocumentReference = mockk()

    private var dao: OrderingAppDao = FakeOrderingDao()

    private val listPurchase = TestData().purchases
    private val listPurchaseDTO = TestData().purchasesDTO

    @Before
    fun setUp() {
        syncPurchaseUseCase = SyncPurchaseRepository(firestore, dao)
    }

    @Test
    fun syncPurchaseRemote() = runTest {
        val completableFuture = CompletableFuture<Unit>()
        every { firestore.collection(FirestoreCollections.purchases) } returns mockk {
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
        syncPurchaseUseCase.syncPurchaseRemote(listPurchase).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        }
        withContext(Dispatchers.IO) {
            completableFuture.get()
        }
    }

    @Test
    fun syncPurchaseLocal() = runTest {
        dao.insertPurchase(listPurchaseDTO.first())
        dao.insertPurchase(listPurchaseDTO[1])
        assertThat(dao.getUnsyncedPurchases()).isNotEmpty()
        syncPurchaseUseCase.syncPurchaseLocal(listPurchase).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            assertThat(dao.getUnsyncedPurchases()).isEmpty()
        }
    }

    @Test
    fun syncPurchaseRemoteException() = runTest {
        every { firestore.collection(FirestoreCollections.purchases) } returns mockk {
            every { document(any()) } throws testException
        }
        syncPurchaseUseCase.syncPurchaseRemote(listPurchase).collect { result ->
            assertError(result)
        }
    }

    @Test
    fun syncPurchaseLocalException() = runTest {
        dao = mockk()
        syncPurchaseUseCase = SyncPurchaseRepository(firestore, dao)
        every { dao.updatePurchaseSync(listPurchase.entries.first().key) } throws testException
        syncPurchaseUseCase.syncPurchaseLocal(listPurchase).collect { result ->
            assertError(result)
        }
    }

    private fun assertError(result: ApiResult<Unit>) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).isEqualTo(TestConstants.testMsgException)
    }
}