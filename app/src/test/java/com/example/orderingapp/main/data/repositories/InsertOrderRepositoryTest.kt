package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.data.utils.TestConstants.testMsgException
import com.example.orderingapp.main.data.utils.TestConstants.testException
import com.example.orderingapp.main.domain.model.Order
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

    @Before
    fun setup() {
        insertOrderUseCase = InsertOrderRepository(dao)
    }

    @Test
    fun insertOrderLocal() = runTest {
        insertOrderUseCase.insertOrderLocal(order).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            assertThat(dao.getOrders()).contains(orderDTO)
        }
    }

    @Test
    fun insertOrderLocalException() = runTest {
        dao = mockk()
        insertOrderUseCase = InsertOrderRepository(dao)
        every { dao.insertOrder(orderDTO) } throws testException
        insertOrderUseCase.insertOrderLocal(order).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }

    private companion object {
        val order = Order(
            id = "123",
            items = listOf(),
            date = "21/12/2021",
            hour = "12:00:00",
            orderValue = 123.0
        )
        val orderDTO = OrderDTO(
            id = "123",
            items = mapOf(),
            dateHour = "12:00:00 21/12/2021",
            orderValue = 123.0,
            synced = false
        )
    }
}