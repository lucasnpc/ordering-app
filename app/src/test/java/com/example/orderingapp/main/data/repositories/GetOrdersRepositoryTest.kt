package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.TestConstants.listItems
import com.example.orderingapp.main.data.utils.TestConstants.listOrder
import com.example.orderingapp.main.data.utils.TestConstants.order
import com.example.orderingapp.main.data.utils.TestConstants.testException
import com.example.orderingapp.main.data.utils.TestConstants.testMsgException
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
    fun getUnsyncedOrders() = runTest {
        every { dao.getUnsyncedOrders() } returns listOrder
        getOrdersUseCase.getUnsyncedOrders(listItems).collect { result ->
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

    @Test
    fun getUnsyncedOrdersException() = runTest {
        every { dao.getUnsyncedOrders() } throws testException
        getOrdersUseCase.getUnsyncedOrders(listItems).collect{ result ->
            assertError(result)
        }
    }

    private fun assertSuccess(result: ApiResult<List<Order>>) {
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        result as ApiResult.Success
        assertThat(result.data[0].id).isEqualTo(order.id)
        assertThat(result.data[0].items[0].id).isEqualTo(order.items[0].id)
        assertThat(result.data[0].items[0].description).isEqualTo(order.items[0].description)
        assertThat(result.data[0].items[0].currentValue).isEqualTo(order.items[0].currentValue)
        assertThat(result.data[0].items[0].minimumStock).isEqualTo(order.items[0].minimumStock)
        assertThat(result.data[0].items[0].currentStock).isEqualTo(order.items[0].currentStock)
        assertThat(result.data[0].items[0].quantity.value).isEqualTo(order.items[0].quantity.value)
        assertThat(result.data[0].date).isEqualTo(order.date)
        assertThat(result.data[0].hour).isEqualTo(order.hour)
        assertThat(result.data[0].orderValue).isEqualTo(order.orderValue)
        assertThat(result.data[0].synced).isEqualTo(order.synced)
    }

    private fun assertError(result: ApiResult<List<Order>>) {
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        result as ApiResult.Error
        assertThat(result.exception.message).contains(testMsgException)
    }
}