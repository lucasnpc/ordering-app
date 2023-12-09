package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.domain.usecase.InsertOrderUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class InsertOrderRepositoryTest {
    private lateinit var insertOrderUseCase: InsertOrderUseCase

    private var dao: OrderingAppDao = FakeOrderingDao()
    private val list = TestData().itemsCompose
    private val listOrder = TestData().orders

    @Before
    fun setup() {
        insertOrderUseCase = InsertOrderRepository(dao)
    }

    @Test
    fun insertOrderLocal() = runTest {
        listOrder.forEach { order ->
            insertOrderUseCase.insertOrderLocal(order.value, list).collect { result ->
                assertThat(result).isInstanceOf(ApiResult.Success::class.java)
                result as ApiResult.Success
                assertThat(result.data.value).isEqualTo(order.value)
            }
        }
    }

    @Test
    fun insertOrderLocalException() = runTest {
        dao = mockk()
        insertOrderUseCase = InsertOrderRepository(dao)
        every { dao.insertOrder(any()) } throws testException
        insertOrderUseCase.insertOrderLocal(listOrder.values.first(), list).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }
}