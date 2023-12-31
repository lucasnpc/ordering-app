package com.example.orderingapp.main.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import com.example.orderingapp.main.domain.model.OrderEntry
import com.example.orderingapp.main.domain.model.PurchaseEntry
import com.example.orderingapp.main.presentation.components.MainNavHost
import com.example.orderingapp.main.presentation.components.OrderingAppBottomBar
import com.example.orderingapp.main.presentation.components.OrderingAppTopBar
import com.example.orderingapp.main.presentation.utils.ScreenList
import com.example.orderingapp.main.presentation.utils.extensions.handleBackEvent
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
                            mainViewModel.unsyncedOrders.size + mainViewModel.unsyncedPurchases.size,
                            mainViewModel
                        )
                    },
                    bottomBar = {
                        OrderingAppBottomBar(navController) {
                            mainViewModel.clearItemsQuantity()
                        }
                    }
                ) { paddingValues ->
                    BackHandler {
                        handleBackEvent(navController) {
                            mainViewModel.clearItemsQuantity()
                        }
                    }
                    MainNavHost(
                        navController = navController,
                        paddingValues = paddingValues,
                        items = mainViewModel.items,
                        finishOrderCallback = { entry ->
                            entry?.let {
                                finishOrder(entry, navController)
                            } ?: Toast.makeText(
                                this,
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        finishPurchaseCallback = { entry ->
                            finishPurchase(entry)
                        }
                    )
                }
            }
        }
    }

    private fun finishOrder(
        orderEntry: OrderEntry,
        navController: NavHostController
    ) {
        if (checkPermissionsEnabled())
            generatePDF(orderEntry)
        navController.navigate(
            ScreenList.VoucherScreen.route + "/${
                Gson().toJson(
                    OrderEntry(
                        orderEntry.key,
                        orderEntry.value
                    )
                )
            }"
        )
        mainViewModel.run {
            setUnsyncedOrder(orderEntry)
        }
    }

    private fun finishPurchase(entry: PurchaseEntry?) {
        var textToShow = getString(R.string.something_went_wrong)
        entry?.let {
            mainViewModel.setUnsyncedPurchase(entry)
            mainViewModel.clearItemsQuantity()
            textToShow = getString(R.string.purchase_saved)
        }
        Toast.makeText(this, textToShow, Toast.LENGTH_SHORT)
            .show()
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