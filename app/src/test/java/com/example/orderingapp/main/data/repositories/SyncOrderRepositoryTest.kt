package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncOrderRepositoryTest {

    private lateinit var syncOrderUseCase: SyncOrderUseCase

    private val firestore: FirebaseFirestore = mockk()
    private val documentReference: DocumentReference = mockk()

    private var dao: OrderingAppDao = FakeOrderingDao()

    private val listOrder = TestData().orders
    private val listOrderDTO = TestData().ordersDTO

    @Before
    fun setUp() {
        syncOrderUseCase = SyncOrderRepository(firestore, dao)
    }

    @Test
    fun syncOrderRemote() = runTest {
        every { firestore.collection("orders") } returns mockk {
            every { document(any()) } returns documentReference
        }
        every { documentReference.set(any()) } returns mockk {
            every { isSuccessful } returns true
        }
        syncOrderUseCase.syncOrderRemote(listOrder).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        }
    }

    @Test
    fun syncOrderLocal() = runTest {
        dao.insertOrder(listOrderDTO.first())
        dao.insertOrder(listOrderDTO[1])
        assertThat(dao.getUnsyncedOrders()).isNotEmpty()
        syncOrderUseCase.syncOrderLocal(listOrder).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            assertThat(dao.getUnsyncedOrders()).isEmpty()
        }
    }

    @Test
    fun syncOrderRemoteException() = runTest {
        every { firestore.collection("orders") } returns mockk {
            every { document(any()) } throws  testException
        }
        syncOrderUseCase.syncOrderRemote(listOrder).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }

    @Test
    fun syncOrderLocalException() = runTest {
        dao = mockk()
        syncOrderUseCase = SyncOrderRepository(firestore, dao)
        every { dao.updateOrderSync(listOrder.first().id) } throws testException
        syncOrderUseCase.syncOrderLocal(listOrder).collect { result ->
            assertError(result)
        }
    }

    private fun assertError(result: ApiResult<Unit>) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).isEqualTo(testMsgException)
    }
}