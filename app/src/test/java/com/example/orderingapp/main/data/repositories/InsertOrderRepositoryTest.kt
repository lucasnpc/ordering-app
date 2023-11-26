package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.data.utils.TestConstants.listItems
import com.example.orderingapp.main.data.utils.TestConstants.order
import com.example.orderingapp.main.data.utils.TestConstants.orderDTO
import com.example.orderingapp.main.data.utils.TestConstants.testException
import com.example.orderingapp.main.data.utils.TestConstants.testMsgException
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
            assertThat(result.data).contains(order)
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