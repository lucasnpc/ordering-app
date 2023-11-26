package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.data.utils.TestConstants.listItems
import com.example.orderingapp.main.data.utils.TestConstants.listOrder
import com.example.orderingapp.main.data.utils.TestConstants.order
import com.example.orderingapp.main.data.utils.TestConstants.testException
import com.example.orderingapp.main.data.utils.TestConstants.testMsgException
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetOrdersRepositoryTest {

    private lateinit var getOrdersUseCase: GetOrdersUseCase
    private val dao: OrderingAppDao = mockk()

    @Before
    fun setUp() {
        getOrdersUseCase = GetOrdersRepository(dao)
    }

    @Test
    fun getOrders() = runTest {
        every { dao.getOrders() } returns listOrder
        getOrdersUseCase.getOrders(listItems).collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun getOrdersException() = runTest {
        every { dao.getOrders() } throws testException
        getOrdersUseCase.getOrders(listItems).collect{ result ->
            assertError(result)
        }
    }

    private fun assertSuccess(result: ApiResult<List<Order>>) {
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        result as ApiResult.Success
        assertThat(result.data).contains(order)
    }

    private fun assertError(result: ApiResult<List<Order>>) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).contains(testMsgException)
    }
}