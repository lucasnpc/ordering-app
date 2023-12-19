package com.example.orderingapp.main.presentation.order

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
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

    private val list = TestData().itemsCompose
    private val listOrder = TestData().orders

    @Before
    fun setUp() {
        orderViewModel = OrderViewModel(mainUseCases)
    }

    @Test
    fun getOrders() = runTest {
        every { mainUseCases.getOrdersUseCase.getOrders() } returns flow {
            emit(ApiResult.Success(listOrder))
        }
        orderViewModel.getOrders()

        assertThat(orderViewModel.orders).isEqualTo(listOrder)
    }

    @Test
    fun getOrdersError() = runTest {
        every { mainUseCases.getOrdersUseCase.getOrders() } returns flow {
            emit(ApiResult.Error(testException))
        }

        orderViewModel.getOrders()

        assertThat(orderViewModel.orders).isEmpty()
    }
}