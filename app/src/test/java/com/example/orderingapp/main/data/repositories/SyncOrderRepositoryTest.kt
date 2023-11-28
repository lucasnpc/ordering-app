package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.commons.TestConstants.order
import com.example.orderingapp.main.commons.TestConstants.orderDTO
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.domain.usecase.SyncOrderUseCase
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncOrderRepositoryTest {

    private lateinit var syncOrderUseCase: SyncOrderUseCase

    private val firestore: FirebaseFirestore = mockk()

    private var dao: OrderingAppDao = FakeOrderingDao()

    @Before
    fun setUp() {
        syncOrderUseCase = SyncOrderRepository(firestore, dao)
    }

    @Test
    fun syncOrderRemote() = runTest {
        every { firestore.collection("orders").add(order).isSuccessful } returns true
        syncOrderUseCase.syncOrderRemote(order).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data)
        }
    }

    @Test
    fun syncOrderLocal() = runTest {
        dao.insertOrder(
            orderDTO
        )
        assertThat(dao.getUnsyncedOrders()).isNotEmpty()
        syncOrderUseCase.syncOrderLocal(order).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            assertThat(dao.getUnsyncedOrders()).isEmpty()
        }
    }

    @Test
    fun syncOrderRemoteException() = runTest {
        every { firestore.collection("orders").add(order).isSuccessful } throws testException
        syncOrderUseCase.syncOrderRemote(order).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }

    @Test
    fun syncOrderLocalException() = runTest {
        dao = mockk()
        syncOrderUseCase = SyncOrderRepository(firestore, dao)
        every { dao.updateOrderSync(order.id) } throws testException
        syncOrderUseCase.syncOrderLocal(order).collect { result ->
            assertError(result)
        }
    }

    private fun assertError(result: ApiResult<Unit>) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).isEqualTo(testMsgException)
    }
}