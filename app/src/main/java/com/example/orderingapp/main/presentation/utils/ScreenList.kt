package com.example.orderingapp.main.presentation.utils

sealed class ScreenList(val route: String) {
    data object MenuScreen : ScreenList("MenuScreen")

    data object OrderScreen : ScreenList("OrderScreen")
    data object StockScreen : ScreenList("StockScreen")
    data object VoucherScreen : ScreenList("VoucherScreen")
}
