package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.commons.safeRequestSuspend
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.domain.model.Item
import com.example.orderingapp.main.domain.model.Order
import com.example.orderingapp.main.domain.usecase.GetOrdersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrdersRepository(private val dao: OrderingAppDao) : GetOrdersUseCase {
    override fun getOrders(_items: List<Item>): Flow<ApiResult<List<Order>>> = flow {
        val result = safeRequestSuspend {
            dao.getOrders().fromOrderDTOToOrder(_items)
        }
        emit(result)
    }

    override fun getUnsyncedOrders(_items: List<Item>): Flow<ApiResult<List<Order>>> = flow {
        val result = safeRequestSuspend {
            dao.getUnsyncedOrders().fromOrderDTOToOrder(_items)
        }
        emit(result)
    }

    private fun List<OrderDTO>.fromOrderDTOToOrder(_items: List<Item>): List<Order> {
        return this.map { orderDTO ->
            Order(
                id = orderDTO.id,
                items = orderDTO.items.toListItem(_items),
                date = orderDTO.dateHour.split(" ")[1],
                hour = orderDTO.dateHour.split(" ")[0],
                orderValue = orderDTO.orderValue
            )
        }
    }

    private fun Map<String, Int>.toListItem(_items: List<Item>): List<Item> {
        val list = this.map {
            _items.find { item -> item.id == it.key }?.let { find ->
                Item(
                    id = find.id,
                    description = find.description,
                    currentValue = find.currentValue,
                    minimumStock = find.minimumStock,
                    currentStock = find.currentStock,
                    quantity = it.value
                )
            } ?: Item()
        }

        return list.filter { it.quantity > 0 }
    }
}