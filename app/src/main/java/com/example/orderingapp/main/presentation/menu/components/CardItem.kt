package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.orderingapp.main.domain.model.ItemCompose

@Composable
fun CardItem(itemCompose: ItemCompose) {
    Card(elevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ItemInfo(itemCompose)
            ChangeItemQuantityButtons(itemCompose)
        }
    }
}
