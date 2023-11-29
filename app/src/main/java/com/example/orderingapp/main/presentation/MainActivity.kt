package com.example.orderingapp.main.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.orderingapp.R
import com.example.orderingapp.commons.pdf.createPDFDocument
import com.example.orderingapp.commons.pdf.writeDocument
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.presentation.components.OrderingAppBottomBar
import com.example.orderingapp.main.presentation.components.OrderingAppTopBar
import com.example.orderingapp.main.presentation.menu.MenuScreen
import com.example.orderingapp.main.presentation.order.OrderScreen
import com.example.orderingapp.main.presentation.stock.StockScreen
import com.example.orderingapp.main.presentation.voucher.VoucherScreen
import com.example.orderingapp.main.theme.OrderingAppTheme
import com.example.orderingapp.main.utils.ScreenList
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

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
                                        Color.White,
                                        colorResource(id = R.color.gray_gradient)
                                    )
                                )
                            )
                    ) {
                        composable(route = ScreenList.MenuScreen.route) {
                            MenuScreen(mainViewModel.items) { list ->
                                mainViewModel.setUnsyncedOrders(list)
                                val order = list.last()
                                if (checkPermissionsEnabled())
                                    generatePDF(order)
                                navController.navigate(
                                    ScreenList.VoucherScreen.route + "/${Gson().toJson(order)}"
                                )
                            }
                        }
                        composable(route = ScreenList.OrderScreen.route) {
                            OrderScreen(mainViewModel.items)
                        }
                        composable(route = ScreenList.StockScreen.route) {
                            StockScreen()
                        }
                        composable(route = ScreenList.VoucherScreen.route + "/{order}",
                            arguments = listOf(
                                navArgument("order") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )) { backStackEntry ->
                            backStackEntry.arguments?.run {
                                VoucherScreen(getString("order").orEmpty())
                            }
                        }
                    }
                }
            }
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

    private fun requestStoragePermission() {
        requestPermissions(
            multiplePermissionNameList.toTypedArray(), REQUEST_CODE
        )
    }

    private fun generatePDF(
        order: Order
    ) {
        lifecycleScope.launch {
            writeDocument(createPDFDocument(order), order)
        }
    }

    private fun checkPermissionsEnabled(): Boolean {
        return multiplePermissionNameList.any {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkPermissionsRationale(): Boolean {
        return multiplePermissionNameList.any {
            ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }
    }

    private companion object {
        const val REQUEST_CODE = 123
    }
}