package com.example.orderingapp.main.presentation.utils

sealed class ScreenList(val route: String) {
    object MenuScreen : ScreenList("MenuScreen")

    object OrderScreen : ScreenList("OrderScreen")
    object StockScreen : ScreenList("StockScreen")
    object VoucherScreen : ScreenList("VoucherScreen")
}
