package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.commons.TestConstants.listItems
import com.example.orderingapp.main.commons.TestConstants.order
import com.example.orderingapp.main.commons.TestConstants.orderDTO
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
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
        insertOrderUseCase.insertOrderLocal(order, listItems).collect { result ->
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
        }
    }

    @Test
    fun insertOrderLocalException() = runTest {
        dao = mockk()
        insertOrderUseCase = InsertOrderRepository(dao)
        every { dao.insertOrder(orderDTO) } throws testException
        insertOrderUseCase.insertOrderLocal(order, listItems).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }
}