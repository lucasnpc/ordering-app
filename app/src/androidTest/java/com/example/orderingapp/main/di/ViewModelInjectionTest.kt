package com.example.orderingapp.main.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.orderingapp.main.domain.fakeusecases.GetItemsUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.GetOrdersUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.GetPurchaseUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.InsertItemsUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.InsertOrderUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.InsertPurchaseUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.SyncOrderUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.SyncPurchaseUseCaseFake
import com.example.orderingapp.main.domain.fakeusecases.UpdateItemsStockUseCaseFake
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.example.orderingapp.main.presentation.MainViewModel
import com.example.orderingapp.main.presentation.menu.MenuViewModel
import com.example.orderingapp.main.presentation.order.OrderViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ViewModelInjectionTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mainUseCases: MainUseCases = MainUseCases(
        getOrdersUseCase = GetOrdersUseCaseFake(),
        insertItemsUseCase = InsertItemsUseCaseFake(),
        getItemsUseCase = GetItemsUseCaseFake(),
        insertOrderUseCase = InsertOrderUseCaseFake(),
        syncOrderUseCase = SyncOrderUseCaseFake(),
        updateItemsStockUseCase = UpdateItemsStockUseCaseFake(),
        insertPurchaseUseCase = InsertPurchaseUseCaseFake(),
        getPurchasesUseCase = GetPurchaseUseCaseFake(),
        syncPurchaseUseCase = SyncPurchaseUseCaseFake()
    )

    @BindValue
    var mainViewModel: MainViewModel = MainViewModel(mainUseCases)

    @BindValue
    var menuViewModel: MenuViewModel = MenuViewModel(mainUseCases)

    @BindValue
    var orderViewModel: OrderViewModel = OrderViewModel(mainUseCases)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun assertMainViewModel() = runTest {
        assertThat(mainViewModel).isNotNull()
    }

    @Test
    fun assertMenuViewModel() = runTest {
        assertThat(menuViewModel).isNotNull()
    }

    @Test
    fun assertOrderViewModel() = runTest {
        assertThat(orderViewModel).isNotNull()
    }
}