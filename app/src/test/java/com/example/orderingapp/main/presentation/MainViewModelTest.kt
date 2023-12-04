package com.example.orderingapp.main.presentation

import com.example.orderingapp.commons.mappings.composeToListItem
import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.MainCoroutineRule
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetItemsUseCase
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.example.orderingapp.main.presentation.utils.GetItemsUseCaseFake
import com.example.orderingapp.main.presentation.utils.GetOrdersUseCaseFake
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

    @Test
    fun getItemsException() = runTest {
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
    fun startSyncing() = runTest {
        mainViewModel.startSyncing()
        assertThat(mainViewModel.unsyncedOrders).isEmpty()
        assertThat(mainViewModel.isSyncing.value).isEqualTo(false)
    }

    @Test
    fun startSyncingExceptionAtRemote() = runTest {
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

    @Test
    fun clearAddedItems() = runTest {
        val getItemsUseCase: GetItemsUseCase = mockk()
        every { getItemsUseCase.getItemsFromRemote() } returns flow { emit(ApiResult.Success(list)) }
        mainUseCases = MainUseCases(
            getItemsUseCase = getItemsUseCase,
            insertItemsUseCase = mockk(),
            getOrdersUseCase = GetOrdersUseCaseFake(),
            insertOrderUseCase = mockk(),
            syncOrderUseCase = SyncOrderUseCaseFake(null, testException)
        )
        mainViewModel = MainViewModel(mainUseCases)
        assertThat(list.values.first().quantity.value).isEqualTo(2)
        assertThat(list.values.toList()[1].quantity.value).isEqualTo(2)

        mainViewModel.clearAddedItems()

        assertThat(list.values.first().quantity.value).isEqualTo(0)
        assertThat(list.values.toList()[1].quantity.value).isEqualTo(0)
    }
}