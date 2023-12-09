package com.example.orderingapp.main.presentation.menu

import com.example.orderingapp.commons.extensions.toDateFormat
import com.example.orderingapp.commons.extensions.toHourFormat
import com.example.orderingapp.commons.mappings.composeToItem
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.repositories.mappings.toOrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MenuViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var menuViewModel: MenuViewModel
    private val mainUseCases: MainUseCases = mockk()

    @Before
    fun setUp() {
        menuViewModel = MenuViewModel(mainUseCases)
    }

    @Test
    fun insertOrder() = runTest {
        val list = TestData().itemsCompose
        val paymentWay = "Pix"
        val addedItems = list.filter { it.value.quantity.value > 0 }
        val createdOrder = Order(
            items = addedItems.composeToItem(),
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            orderValue = addedItems.values.sumOf { it.item.currentValue * it.item.finalQuantity },
            paymentWay = paymentWay
        )
        val expectedOrder = Order(
            items = mapOf(
                "1" to Item(
                    description = "item 1",
                    currentValue = 1.0,
                    finalQuantity = 2
                ), "2" to Item(
                    description = "item 2",
                    currentValue = 1.0,
                    finalQuantity = 2
                )
            ),
            hour = createdOrder.hour,
            date = createdOrder.date,
            orderValue = createdOrder.orderValue,
            paymentWay = createdOrder.paymentWay
        )
        every {
            mainUseCases.insertOrderUseCase.insertOrderLocal(
                createdOrder,
                list
            )
        } returns flow {
            emit(
                ApiResult.Success(
                    OrderEntry(createdOrder.toOrderDTO().id, createdOrder)
                )
            )
        }

        menuViewModel.insertOrder(createdOrder, list) {
            assertThat(it.value).isEqualTo(expectedOrder)
        }
    }

    @Test
    fun insertOrderError() = runTest {
        val list = TestData().itemsCompose
        val paymentWay = "Pix"
        val addedItems = list.filter { it.value.quantity.value > 0 }
        val _order = Order(
            items = addedItems.composeToItem(),
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            orderValue = addedItems.values.sumOf { it.item.currentValue * it.item.finalQuantity },
            paymentWay = paymentWay
        )
        every { mainUseCases.insertOrderUseCase.insertOrderLocal(_order, any()) } returns flow {
            emit(
                ApiResult.Error(RuntimeException())
            )
        }

        menuViewModel.insertOrder(_order, list) {
            assertThat(it).isNull()
        }
    }
}