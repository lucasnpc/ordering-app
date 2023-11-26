package com.example.orderingapp.main.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun OrderingAppTopBar() {
    TopAppBar(title = {
        Text(text = "Ordering App", fontSize = 20.sp)
    }, actions = {
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notification",
                tint = MaterialTheme.colors.onPrimary,
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    })
}