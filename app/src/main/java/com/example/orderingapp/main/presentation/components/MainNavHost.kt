package com.example.orderingapp.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.orderingapp.R
import com.example.orderingapp.main.domain.model.ItemCompose
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.presentation.menu.MenuScreen
import com.example.orderingapp.main.presentation.order.OrderScreen
import com.example.orderingapp.main.presentation.stock.StockScreen
import com.example.orderingapp.main.presentation.utils.ScreenList
import com.example.orderingapp.main.presentation.voucher.VoucherScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    items: Map<String, ItemCompose>,
    finishOrderCallback: (OrderEntry?) -> Unit
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = ScreenList.MenuScreen.route,
        modifier = androidx.compose.ui.Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        colorResource(id = R.color.gray_gradient)
                    )
                )
            )
    ) {
        composable(route = ScreenList.MenuScreen.route) {
            MenuScreen(items) { orderEntry ->
                finishOrderCallback(orderEntry)
            }
        }
        composable(route = ScreenList.OrderScreen.route) {
            OrderScreen()
        }
        composable(route = ScreenList.StockScreen.route) {
            StockScreen()
        }
        composable(route = ScreenList.VoucherScreen.route + "/{order}",
            arguments = listOf(
                navArgument(context.getString(R.string.order_argument)) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) { backStackEntry ->
            backStackEntry.arguments?.let { argument ->
                VoucherScreen(
                    argument.getString(stringResource(id = R.string.order_argument)).orEmpty()
                )
            }
        }
    }
}