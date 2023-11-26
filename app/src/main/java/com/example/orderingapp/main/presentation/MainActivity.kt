package com.example.orderingapp.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.orderingapp.main.presentation.components.OrderingAppBottomBar
import com.example.orderingapp.main.presentation.components.OrderingAppTopBar
import com.example.orderingapp.main.theme.OrderingAppTheme
import com.example.orderingapp.main.presentation.screens.MenuScreen
import com.example.orderingapp.main.presentation.screens.OrderScreen
import com.example.orderingapp.main.utils.ScreenList
import com.example.orderingapp.main.presentation.screens.StockScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            OrderingAppTheme {
                Scaffold(
                    topBar = {
                        OrderingAppTopBar()
                    },
                    bottomBar = {
                        OrderingAppBottomBar(navController)
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = ScreenList.MenuScreen.route,
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFFFFFFF),
                                        Color(0xFFC4C4C4)
                                    )
                                )
                            )
                    ) {
                        composable(route = ScreenList.MenuScreen.route) {
                            MenuScreen()
                        }
                        composable(route = ScreenList.OrderScreen.route) {
                            OrderScreen()
                        }
                        composable(route = ScreenList.StockScreen.route) {
                            StockScreen()
                        }
                    }
                }
            }
        }
    }
}