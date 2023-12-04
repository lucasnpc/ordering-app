package com.example.orderingapp.main.presentation.components

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Badge
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.orderingapp.R
import com.example.orderingapp.commons.extensions.finishSession
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.presentation.MainViewModel
import com.example.orderingapp.main.presentation.utils.ScreenList
import com.example.orderingapp.main.theme.redPrimary

@Composable
fun OrderingAppTopBar(
    navController: NavHostController,
    unsyncedOrders: List<Order>,
    mainViewModel: MainViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current as? Activity
    val rotation by animateFloatAsState(
        targetValue = if (mainViewModel.isSyncing.value) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
        )
    )
    TopAppBar(title = {
        Text(text = getCurrentPage(navBackStackEntry), fontSize = 20.sp)
    }, actions = {
        Box {
            if (unsyncedOrders.isNotEmpty())
                Badge(backgroundColor = Color.Black) {
                    Text(
                        text = unsyncedOrders.size.toString(),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .background(Color.Black),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            IconButton(
                onClick = { mainViewModel.startSyncing() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = stringResource(R.string.sync),
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.rotate(if (mainViewModel.isSyncing.value) rotation else 0f)
                )
            }
        }
        IconButton(
            onClick = {
                context?.finishSession()
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = stringResource(R.string.logout),
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    }, navigationIcon = {
        IconButton(onClick = {
            if (!navController.popBackStack()) {
                context?.finishSession()
            }
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }
    },
        backgroundColor = redPrimary
    )
}

@Composable
private fun getCurrentPage(navBackStackEntry: NavBackStackEntry?): String {
    return navBackStackEntry?.destination?.route?.run {
        when {
            this == ScreenList.MenuScreen.route -> stringResource(id = R.string.menu_name)
            this == ScreenList.OrderScreen.route -> stringResource(id = R.string.order_name)
            this == ScreenList.StockScreen.route -> stringResource(id = R.string.stock_name)
            this.contains(ScreenList.VoucherScreen.route) -> stringResource(id = R.string.voucher_name)
            else -> stringResource(id = R.string.app_name)
        }
    }.orEmpty()
}