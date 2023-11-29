package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.domain.model.ItemCompose
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
    private val list =
        listOf(ItemCompose(TestData().items.first()), ItemCompose(TestData().items[1]))
    private val listOrder = TestData().orders
    private val listOrderDTO = TestData().ordersDTO

    @Before
    fun setup() {
        insertOrderUseCase = InsertOrderRepository(dao)
    }

    @Test
    fun insertOrderLocal() = runTest {
        listOrder.forEach { order ->
            insertOrderUseCase.insertOrderLocal(order, list).collect { result ->
                assertThat(result).isInstanceOf(ApiResult.Success::class.java)
                result as ApiResult.Success
                assertThat(result.data).contains(order)
            }
        }
    }

    @Test
    fun insertOrderLocalException() = runTest {
        dao = mockk()
        insertOrderUseCase = InsertOrderRepository(dao)
        every { dao.insertOrder(listOrderDTO.first()) } throws testException
        insertOrderUseCase.insertOrderLocal(listOrder.first(), list).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }
}