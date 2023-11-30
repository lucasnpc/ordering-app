package com.example.orderingapp.main.presentation.order

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var orderViewModel: OrderViewModel
    private val mainUseCases: MainUseCases = mockk()

    private val list =
        listOf(ItemCompose(TestData().items.first()), ItemCompose(TestData().items[1]))
    private val listOrder = TestData().orders

    @Before
    fun setUp() {
        orderViewModel = OrderViewModel(mainUseCases)
    }

    @Test
    fun getOrders() = runTest {
        every { mainUseCases.getOrdersUseCase.getOrders(list) } returns flow {
            emit(ApiResult.Success(listOrder))
        }
        orderViewModel.getOrders(list)

        assertThat(orderViewModel.orders).isEqualTo(listOrder.reversed())
    }

    @Test
    fun getOrdersError() = runTest {
        every { mainUseCases.getOrdersUseCase.getOrders(list) } returns flow {
            emit(ApiResult.Error(testException))
        }

        orderViewModel.getOrders(list)

        assertThat(orderViewModel.orders).isEmpty()
    }
}