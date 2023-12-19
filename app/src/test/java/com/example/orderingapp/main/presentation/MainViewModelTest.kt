package com.example.orderingapp.main.presentation

import com.example.orderingapp.main.presentation.utils.mappings.composeToItem
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.model.Purchase
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.example.orderingapp.main.presentation.utils.GetItemsUseCaseFake
import com.example.orderingapp.main.presentation.utils.GetOrdersUseCaseFake
import com.example.orderingapp.main.presentation.utils.InsertItemsUseCaseFake
import com.example.orderingapp.main.presentation.utils.SyncOrderUseCaseFake
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mainViewModel: MainViewModel
    private var mainUseCases: MainUseCases = MainUseCases(
        getItemsUseCase = GetItemsUseCaseFake(),
        insertItemsUseCase = InsertItemsUseCaseFake(),
        getOrdersUseCase = GetOrdersUseCaseFake(),
        insertOrderUseCase = mockk(),
        syncOrderUseCase = SyncOrderUseCaseFake(),
        updateItemsStockUseCase = mockk(),
        insertPurchaseUseCase = mockk()
    )

    private val list = TestData().itemsCompose
    private val listOrders = TestData().orders
    private val listPurchases = TestData().purchases
    private val addedOrder =
        Order(
            items = list.composeToItem(),
            date = "21 Dec 2022",
            hour = "12:00",
            orderValue = 10.0,
            paymentWay = "Crédito"
        )
    private val addedPurchase =
        Purchase(
            items = list.composeToItem(),
            date = "21 Dec 2022",
            hour = "12:00",
            purchaseValue = 10.0,
            paymentWay = "Crédito"
        )

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mainUseCases)
    }

    @Test
    fun getUnsyncedOrders() = runTest {
        assertThat(mainViewModel.unsyncedOrders).isEqualTo(listOrders)
    }

    @Test
    fun getUnsyncedOrdersException() = runTest {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(),
            insertItemsUseCase = InsertItemsUseCaseFake(),
            getOrdersUseCase = GetOrdersUseCaseFake(testException),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(),
            updateItemsStockUseCase = mockk(),
            insertPurchaseUseCase = mockk()
        )
        mainViewModel = MainViewModel(mainUseCases)
        assertThat(mainViewModel.unsyncedOrders).isEmpty()
    }

    @Test
    fun getItems() {
        assertItemsEqualTo()
    }

    @Test
    fun getItemLocalForRemoteException() {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(testException, null),
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(),
            updateItemsStockUseCase = mockk(),
            insertPurchaseUseCase = mockk()
        )
        mainViewModel = MainViewModel(mainUseCases)

        assertItemsEqualTo()
    }

    @Test
    fun getItemsLocalException() = runTest {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(testException, testException),
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(),
            updateItemsStockUseCase = mockk(),
            insertPurchaseUseCase = mockk()
        )
        mainViewModel = MainViewModel(mainUseCases)
        assertThat(mainViewModel.items).isEmpty()
    }

    @Test
    fun setUnsyncedOrders() = runTest {
        val listAdded = listOrders.toMutableMap()
        listAdded["3"] = addedOrder
        mainViewModel.setUnsyncedOrder(OrderEntry("3", addedOrder))

        assertThat(mainViewModel.unsyncedOrders).isNotEqualTo(listOrders)
        assertThat(mainViewModel.unsyncedOrders).isEqualTo(listAdded)
    }

    @Test
    fun setUnsyncedPurchases() = runTest {
        val listAdded = listPurchases.toMutableMap()
        listAdded["3"] = addedPurchase
        mainViewModel.setUnsyncedPurchase(PurchaseEntry("3", addedPurchase))

        assertThat(mainViewModel.unsyncedPurchases).isNotEqualTo(listPurchases)
        assertThat(mainViewModel.unsyncedPurchases).isEqualTo(listAdded)
    }

    @Test
    fun startSyncing() = runTest {
        mainViewModel.startSyncing()
        assertThat(mainViewModel.unsyncedOrders).isEmpty()
        assertThat(mainViewModel.isSyncing.value).isEqualTo(false)
    }

    @Test
    fun startSyncingExceptionAtRemote() = runTest {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(),
            insertItemsUseCase = InsertItemsUseCaseFake(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(testException, null),
            updateItemsStockUseCase = mockk(),
            insertPurchaseUseCase = mockk()
        )

        mainViewModel = MainViewModel(mainUseCases)

        mainViewModel.startSyncing()
        assertThat(mainViewModel.unsyncedOrders).isNotEmpty()
        assertThat(mainViewModel.isSyncing.value).isEqualTo(false)
    }

    @Test
    fun startSyncingExceptionAtLocal() {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(),
            insertItemsUseCase = InsertItemsUseCaseFake(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(null, testException),
            updateItemsStockUseCase = mockk(),
            insertPurchaseUseCase = mockk()
        )
        mainViewModel = MainViewModel(mainUseCases)

        mainViewModel.startSyncing()
        assertThat(mainViewModel.unsyncedOrders).isNotEmpty()
        assertThat(mainViewModel.isSyncing.value).isEqualTo(false)
    }

    @Test
    fun clearAddedItems() = runTest {
        val getItemsUseCase: GetItemsUseCase = mockk()
        every { getItemsUseCase.getItemsFromRemote() } returns flow { emit(ApiResult.Success(list)) }
        mainUseCases = MainUseCases(
            getItemsUseCase = getItemsUseCase,
            insertItemsUseCase = InsertItemsUseCaseFake(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(null, testException),
            updateItemsStockUseCase = mockk(),
            insertPurchaseUseCase = mockk()
        )
        mainViewModel = MainViewModel(mainUseCases)
        assertThat(list.values.first().quantity.value).isEqualTo(2)
        assertThat(list.values.toList()[1].quantity.value).isEqualTo(2)

        mainViewModel.clearAddedItems()

        assertThat(list.values.first().quantity.value).isEqualTo(0)
        assertThat(list.values.toList()[1].quantity.value).isEqualTo(0)
    }

    private fun assertItemsEqualTo() {
        for ((key, value) in mainViewModel.items) {
            val expected = list[key]?.item
            value.item.run {
                assertThat(description).isEqualTo(expected?.description)
                assertThat(currentValue).isEqualTo(expected?.currentValue)
                assertThat(currentStock).isEqualTo(expected?.currentStock)
                assertThat(minimumStock).isEqualTo(expected?.minimumStock)
                assertThat(finalQuantity).isEqualTo(expected?.finalQuantity)
            }
        }
    }
}