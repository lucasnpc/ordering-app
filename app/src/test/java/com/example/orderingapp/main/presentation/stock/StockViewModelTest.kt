package com.example.orderingapp.main.presentation.stock

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.repositories.mappings.toPurchaseDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.example.orderingapp.main.presentation.utils.extensions.toDateFormat
import com.example.orderingapp.main.presentation.utils.extensions.toHourFormat
import com.example.orderingapp.main.presentation.utils.mappings.composeToItem
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StockViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var stockViewModel: StockViewModel
    private val mainUseCases = mockk<MainUseCases>()

    @Before
    fun setUp() {
        stockViewModel = StockViewModel(mainUseCases)
    }

    @Test
    fun insertPurchase() = runTest {
        val list = TestData().itemsCompose
        val paymentWay = "Pix"
        val addedItems = list.filter { it.value.quantity.value > 0 }
        val createdPurchase = Purchase(
            items = addedItems.composeToItem(),
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            purchaseValue = addedItems.values.sumOf { it.item.costValue * it.item.finalQuantity },
            paymentWay = paymentWay
        )
        val expectedPurchase = Purchase(
            items = addedItems.composeToItem(),
            hour = createdPurchase.hour,
            date = createdPurchase.date,
            purchaseValue = createdPurchase.purchaseValue,
            paymentWay = createdPurchase.paymentWay
        )
        every { mainUseCases.insertPurchaseUseCase.insertPurchaseLocal(createdPurchase) } returns flow {
            emit(
                ApiResult.Success(
                    PurchaseEntry(
                        createdPurchase.toPurchaseDTO().id,
                        createdPurchase
                    )
                )
            )
        }

        stockViewModel.insertPurchase(createdPurchase) {
            assertThat(it?.value).isEqualTo(expectedPurchase)
        }
    }

    @Test
    fun insertPurchaseError() = runTest {
        val list = TestData().itemsCompose
        val paymentWay = "Pix"
        val addedItems = list.filter { it.value.quantity.value > 0 }
        val createdPurchase = Purchase(
            items = addedItems.composeToItem(),
            hour = System.currentTimeMillis().toHourFormat(),
            date = System.currentTimeMillis().toDateFormat(),
            purchaseValue = addedItems.values.sumOf { it.item.costValue * it.item.finalQuantity },
            paymentWay = paymentWay
        )
        every { mainUseCases.insertPurchaseUseCase.insertPurchaseLocal(createdPurchase) } returns flow {
            emit(ApiResult.Error(RuntimeException()))
        }

        stockViewModel.insertPurchase(createdPurchase) {
            assertThat(it).isNull()
        }
    }
}