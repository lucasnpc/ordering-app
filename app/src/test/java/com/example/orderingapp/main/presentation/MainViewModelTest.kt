package com.example.orderingapp.main.presentation

import com.example.orderingapp.commons.mappings.composeToListItem
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.example.orderingapp.main.presentation.utils.GetItemsUseCaseFake
import com.example.orderingapp.main.presentation.utils.GetOrdersUseCaseFake
import com.example.orderingapp.main.presentation.utils.SyncOrderUseCaseFake
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
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
        insertItemsUseCase = mockk(),
        getOrdersUseCase = GetOrdersUseCaseFake(),
        insertOrderUseCase = mockk(),
        syncOrderUseCase = SyncOrderUseCaseFake()
    )

    private val list = TestData().itemsCompose
    private val listOrders = TestData().orders
    private val addedOrder =
        Order(
            items = list.composeToListItem(),
            date = "21 Dec 2022",
            hour = "12:00",
            orderValue = 10.0,
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
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(testException),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake()
        )
        mainViewModel = MainViewModel(mainUseCases)
        assertThat(mainViewModel.unsyncedOrders).isEmpty()
    }

    @Test
    fun getItems() {
        for (i in mainViewModel.items.indices) {
            mainViewModel.items[i].items.run {
                assertThat(id).isEqualTo(list[i].items.id)
                assertThat(description).isEqualTo(list[i].items.description)
                assertThat(currentValue).isEqualTo(list[i].items.currentValue)
                assertThat(currentStock).isEqualTo(list[i].items.currentStock)
                assertThat(minimumStock).isEqualTo(list[i].items.minimumStock)
                assertThat(finalQuantity).isEqualTo(list[i].items.finalQuantity)
            }
        }
    }

    @Test
    fun getItemsException() {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(testException),
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake()
        )
        mainViewModel = MainViewModel(mainUseCases)
        assertThat(mainViewModel.items).isEmpty()
    }

    @Test
    fun setUnsyncedOrders() = runTest {
        val listAdded = listOrders.toMutableList()
        listAdded.add(addedOrder)
        mainViewModel.setUnsyncedOrders(listAdded)

        assertThat(mainViewModel.unsyncedOrders).isNotEqualTo(listOrders)
        assertThat(mainViewModel.unsyncedOrders).isEqualTo(listAdded)
    }

    @Test
    fun startSyncing() {
        mainViewModel.startSyncing()
        assertThat(mainViewModel.unsyncedOrders).isEmpty()
        assertThat(mainViewModel.isSyncing.value).isEqualTo(false)
    }

    @Test
    fun startSyncingExceptionAtRemote() {
        mainUseCases = MainUseCases(
            getItemsUseCase = GetItemsUseCaseFake(),
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(testException, null)
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
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(null, testException)
        )
        mainViewModel = MainViewModel(mainUseCases)

        mainViewModel.startSyncing()
        assertThat(mainViewModel.unsyncedOrders).isNotEmpty()
        assertThat(mainViewModel.isSyncing.value).isEqualTo(false)
    }
}