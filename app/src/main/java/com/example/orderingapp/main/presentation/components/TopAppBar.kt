package com.example.orderingapp.main.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.orderingapp.R
import com.example.orderingapp.main.utils.ScreenList

@Composable
fun OrderingAppTopBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    TopAppBar(title = {
        Text(text = getCurrentPage(navBackStackEntry), fontSize = 20.sp)
    }, actions = {
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Filled.Sync,
                contentDescription = stringResource(R.string.sync),
                tint = MaterialTheme.colors.onPrimary,
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = stringResource(R.string.logout),
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    })
}

@Composable
private fun getCurrentPage(navBackStackEntry: NavBackStackEntry?): String {
    return when (navBackStackEntry?.destination?.route) {
        ScreenList.MenuScreen.route -> stringResource(id = R.string.menu_name)
        ScreenList.MenuScreen.route -> stringResource(id = R.string.order_name)
        ScreenList.MenuScreen.route -> stringResource(id = R.string.stock_name)
        else -> stringResource(id = R.string.app_name)
    }

}