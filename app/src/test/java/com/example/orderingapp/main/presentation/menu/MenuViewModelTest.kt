package com.example.orderingapp.main.presentation.menu

import com.example.orderingapp.commons.extensions.toDateFormat
import com.example.orderingapp.commons.extensions.toHourFormat
import com.example.orderingapp.commons.mappings.composeToListItem
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.repositories.mappings.fromOrderDTOToOrder
import com.example.orderingapp.main.data.repositories.mappings.toOrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
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
        val addedItems = list.filter { it.quantity.value > 0 }
        val createdOrder = Order(
            items = addedItems.composeToListItem(),
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            orderValue = addedItems.sumOf { it.items.currentValue * it.items.finalQuantity },
            paymentWay = paymentWay
        )
        val expectedOrder = Order(
            id = createdOrder.id,
            items = listOf(
                Item(
                    id = "1",
                    description = "item 1",
                    currentValue = 1.0,
                    finalQuantity = 2
                ), Item(
                    id = "2",
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
                    listOf(createdOrder.toOrderDTO()).fromOrderDTOToOrder(addedItems.composeToListItem())
                )
            )
        }

        assertThat(list[0].items.finalQuantity).isEqualTo(2)
        assertThat(list[1].items.finalQuantity).isEqualTo(2)

        menuViewModel.insertOrder(createdOrder, list) {
            assertThat(it).contains(expectedOrder)
        }

        assertThat(list[0].items.finalQuantity).isEqualTo(0)
        assertThat(list[1].items.finalQuantity).isEqualTo(0)
    }

    @Test
    fun insertOrderError() = runTest {
        val list = TestData().itemsCompose
        val paymentWay = "Pix"
        val addedItems = list.filter { it.items.finalQuantity > 0 }
        val _order = Order(
            items = addedItems.composeToListItem(),
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            orderValue = addedItems.sumOf { it.items.currentValue * it.items.finalQuantity },
            paymentWay = paymentWay
        )
        every { mainUseCases.insertOrderUseCase.insertOrderLocal(_order, any()) } returns flow {
            emit(
                ApiResult.Error(RuntimeException())
            )
        }


        assertThat(list[0].items.finalQuantity).isEqualTo(2)
        assertThat(list[1].items.finalQuantity).isEqualTo(2)

        menuViewModel.insertOrder(_order, list) {
            assertThat(it).isEmpty()
        }

        assertThat(list[0].items.finalQuantity).isEqualTo(0)
        assertThat(list[1].items.finalQuantity).isEqualTo(0)
    }
}