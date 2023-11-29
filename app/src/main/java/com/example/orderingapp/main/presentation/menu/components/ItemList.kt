package com.example.orderingapp.main.presentation.menu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.ItemCompose

@Composable
fun ItemsList(list: List<ItemCompose>) {
    Box(modifier = Modifier.padding(PaddingValues(8.dp))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { itemCompose ->
                Card(elevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = itemCompose.item.description,
                                fontSize = 18.sp,
                                modifier = Modifier.width(180.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = itemCompose.item.currentValue.currencyFormat(), fontSize = 18.sp)
                        }
                        Column {
                            Row {
                                OutlinedButton(
                                    onClick = {
                                        if (itemCompose.quantity.value > 0) {
                                            itemCompose.quantity.value--
                                            itemCompose.item.finalQuantity = itemCompose.quantity.value
                                        }
                                    },
                                    modifier = Modifier.width(40.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "-")
                                }
                                OutlinedButton(
                                    onClick = {},
                                    modifier = Modifier.width(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = itemCompose.quantity.value.toString())
                                }
                                OutlinedButton(
                                    onClick = {
                                        itemCompose.quantity.value++
                                        itemCompose.item.finalQuantity = itemCompose.quantity.value
                                    },
                                    modifier = Modifier.width(40.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        contentColor = Color.White
                                    )

                                ) {
                                    Text(text = "+")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}