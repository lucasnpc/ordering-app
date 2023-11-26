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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.orderingapp.R

@Composable
fun OrderingAppTopBar() {
    TopAppBar(title = {
        Text(text = stringResource(id = R.string.app_name), fontSize = 20.sp)
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