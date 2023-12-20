package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.OrderDTO
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
    private val listOrdersDTO = TestData().ordersDTO.toMutableList()
    private val listOrders = TestData().orders

    @Before
    fun setUp() {
        getOrdersUseCase = GetOrdersRepository(dao)
    }

    @Test
    fun getOrders() = runTest {
        every { dao.getOrders() } returns listOrdersDTO
        getOrdersUseCase.getOrders().collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun getOrdersWithEmptyItems() = runTest {
        listOrdersDTO.add(
            OrderDTO(
                id = "3", items = mapOf(), date = "21 Dec 2021",
                hour = "12:00",
                orderValue = 10.0,
                paymentWay = "Pix",
                synced = false
            )
        )
        every { dao.getOrders() } returns listOrdersDTO
        every { dao.deleteOrder(listOrdersDTO.last()) } answers {
            listOrdersDTO.removeLast()
        }
        getOrdersUseCase.getOrders().collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun getUnsyncedOrders() = runTest {
        every { dao.getUnsyncedOrders() } returns listOrdersDTO
        getOrdersUseCase.getUnsyncedOrders().collect { result ->
            assertSuccess(result)
        }
    }

    @Test
    fun getOrdersException() = runTest {
        every { dao.getOrders() } throws testException
        getOrdersUseCase.getOrders().collect { result ->
            assertError(result)
        }
    }

    @Test
    fun getUnsyncedOrdersException() = runTest {
        every { dao.getUnsyncedOrders() } throws testException
        getOrdersUseCase.getUnsyncedOrders().collect { result ->
            assertError(result)
        }
    }

    private fun assertSuccess(result: ApiResult<Map<String, Order>>) {
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        result as ApiResult.Success
        assertThat(result.data).isEqualTo(listOrders)
    }

    private fun assertError(result: ApiResult<Map<String, Order>>) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).contains(testMsgException)
    }
}