package com.example.orderingapp.main.presentation.order

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orderingapp.commons.extensions.currencyFormat
import com.example.orderingapp.main.domain.model.ItemCompose

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun OrderScreen(list: List<ItemCompose>, orderViewModel: OrderViewModel = hiltViewModel()) {
    orderViewModel.getOrders(list)
    Box(modifier = Modifier.padding(PaddingValues(8.dp))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stickyHeader {
                Text(
                    text = "Pedidos Feitos",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.onBackground)
                        .padding(top = 16.dp, bottom = 16.dp)
                        .align(Alignment.TopCenter),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            items(orderViewModel.orders) { order ->
                Card(elevation = 8.dp, modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(
                            PaddingValues(8.dp)
                        )
                    ) {
                        Text(
                            text = "Pedido concluído em ${order.date} às ${order.hour}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Items:",
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        order.items.forEach {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = it.description,
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.width(150.dp),
                                    maxLines = 1
                                )
                                Text(
                                    text = "Qtd.${it.finalQuantity}",
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = (it.currentValue * it.finalQuantity).currencyFormat(),
                                    fontSize = 16.sp,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Total: ${order.orderValue.currencyFormat()}",
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
    }
}