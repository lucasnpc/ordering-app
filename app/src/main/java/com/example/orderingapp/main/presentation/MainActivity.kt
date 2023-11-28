package com.example.orderingapp.main.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.currencyFormat
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
                            MenuScreen { list ->
                                mainViewModel.setUnsyncedOrders(list)
                                val order = list.last()
                                if (checkPermissionsEnabled())
                                    createPdfDocument(order)
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

    override fun onResume() {        super.onResume()
        when {
            !checkPermissionsEnabled() -> {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), REQUEST_CODE
                )
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                Toast.makeText(
                    this,
                    "Por favor, precisamos de permissão para emitir seus compovantes",
                    Toast.LENGTH_SHORT
                ).show()
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), REQUEST_CODE
                )
            }
        }
    }

    private fun createPdfDocument(
        order: Order
    ) {
        val pageHeight = 1120
        val pagewidth = 792
        val doc = PdfDocument()
        val title = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 16F
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
        }
        val text = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = 16F
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
        }
        val mypageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage: PdfDocument.Page = doc.startPage(mypageInfo)
        val canvas = myPage.canvas

        canvas.apply {
            drawText(getString(R.string.payment_voucher), 396F, 80F, title)
            drawText("${order.date} ${order.hour}", 396F, 120F, text)
            drawText("PEDIDO: ", 396F, 160F, title)
            var height = 160F
            order.items.forEach { item ->
                height += 20f
                this.drawText(
                    "Item: ${item.description} Qtd. ${item.quantity.value}",
                    396F,
                    height,
                    text
                )
            }
            height += 40
            drawText(
                "VALOR: ${
                    order.items.sumOf { it.quantity.value * it.currentValue }.currencyFormat()
                }", 396F, height, title
            )
            drawText("PAGAMENTO: ${order.paymentWay}", 396F, height + 20, title)
        }

        doc.finishPage(myPage)

        mainViewModel.writeDoc(
            doc, this, order
        )
    }

    private fun checkPermissionsEnabled(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private companion object {
        const val REQUEST_CODE = 123
    }
}