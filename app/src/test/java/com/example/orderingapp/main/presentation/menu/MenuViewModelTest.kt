package com.example.orderingapp.main.presentation.menu

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.commons.extensions.toDateFormat
import com.example.orderingapp.commons.extensions.toHourFormat
import com.example.orderingapp.main.data.repositories.mappings.fromOrderDTOToOrder
import com.example.orderingapp.main.data.repositories.mappings.toOrderDTO
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestConstants.listItems
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.assertListItemEqualsTo
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
        every { mainUseCases.getItemsUseCase.getItemsFromRemote() } returns flow {
            emit(ApiResult.Success(listItems))
        }

        menuViewModel = MenuViewModel(mainUseCases)
    }

    @Test
    fun getItems() = runTest {
        assertThat(menuViewModel.items).contains(listItems[0])
    }

    @Test
    fun getItemsError() = runTest {
        every { mainUseCases.getItemsUseCase.getItemsFromRemote() } returns flow {
            emit(ApiResult.Error(testException))
        }

        menuViewModel = MenuViewModel(mainUseCases)

        assertThat(menuViewModel.items).isEmpty()
    }

    @Test
    fun insertOrder() = runTest {
        val paymentWay = "Pix"
        val addedItems = listItems.filter { it.finalQuantity > 0 }
        val _order = Order(
            items = addedItems,
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            orderValue = addedItems.sumOf { it.currentValue * it.quantity.value },
            paymentWay = paymentWay
        )
        every { mainUseCases.insertOrderUseCase.insertOrderLocal(_order, any()) } returns flow {
            emit(
                ApiResult.Success(
                    listOf(_order.toOrderDTO()).fromOrderDTOToOrder(addedItems)
                )
            )
        }

        assertThat(addedItems[0].finalQuantity).isEqualTo(2)
        assertThat(addedItems[1].finalQuantity).isEqualTo(2)

        menuViewModel.insertOrder(_order) {
            it.find { unsyncedOrder -> unsyncedOrder.id == _order.id }?.let { find ->
                find.items.assertListItemEqualsTo(listItems)
                assertThat(find.id).isEqualTo(_order.id)
            }
        }

        assertThat(menuViewModel.items).contains(addedItems[0])
        assertThat(addedItems[0].finalQuantity).isEqualTo(0)
        assertThat(addedItems[1].finalQuantity).isEqualTo(0)
    }

    @Test
    fun insertOrderError() = runTest {
        val paymentWay = "Pix"
        val addedItems = listItems.filter { it.finalQuantity > 0 }
        val _order = Order(
            items = addedItems,
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            orderValue = addedItems.sumOf { it.currentValue * it.quantity.value },
            paymentWay = paymentWay
        )
        every { mainUseCases.insertOrderUseCase.insertOrderLocal(_order, any()) } returns flow {
            emit(
                ApiResult.Error(RuntimeException())
            )
        }

        menuViewModel.insertOrder(_order) {
            assertThat(it).isEmpty()
        }
    }
}