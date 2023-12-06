package com.example.orderingapp.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.Scaffold
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.orderingapp.R
import com.example.orderingapp.commons.pdf.PdfUtil.generatePDF
import com.example.orderingapp.commons.permissions.PermissionsUtil.checkPermissionsEnabled
import com.example.orderingapp.commons.permissions.PermissionsUtil.checkPermissionsRationale
import com.example.orderingapp.commons.permissions.PermissionsUtil.requestStoragePermission
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.presentation.components.MainNavHost
import com.example.orderingapp.main.presentation.components.OrderingAppBottomBar
import com.example.orderingapp.main.presentation.components.OrderingAppTopBar
import com.example.orderingapp.main.presentation.utils.ScreenList
import com.example.orderingapp.main.theme.OrderingAppTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            OrderingAppTheme {
                Scaffold(
                    topBar = {
                        OrderingAppTopBar(
                            navController,
                            mainViewModel.unsyncedOrders,
                            mainViewModel
                        )
                    },
                    bottomBar = {
                        OrderingAppBottomBar(navController)
                    }
                ) { paddingValues ->
                    MainNavHost(
                        navController = navController,
                        paddingValues = paddingValues,
                        items = mainViewModel.items
                    ) { list ->
                        finishOrderCallback(list, navController)
                    }
                }
            }
        }
    }

    private fun finishOrderCallback(
        list: List<Order>,
        navController: NavHostController
    ) {
        list.lastOrNull()?.let {
            if (checkPermissionsEnabled())
                generatePDF(it)
            navController.navigate(
                ScreenList.VoucherScreen.route + "/${Gson().toJson(it)}"
            )
        }
        mainViewModel.run {
            setUnsyncedOrders(list)
            clearAddedItems()
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            checkPermissionsRationale() -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.needed_permission))
                    .setMessage(getString(R.string.asking_permission))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        requestStoragePermission()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
                    .create().show()
            }
            !checkPermissionsEnabled() -> {
                requestStoragePermission()
            }
        }
    }
}