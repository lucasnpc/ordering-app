package com.example.orderingapp.main.data.utils

import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO

class FakeOrderingDao : OrderingAppDao {
    private val itemDTOS = arrayListOf<ItemDTO>()
    private val orderDTOS = arrayListOf<OrderDTO>()
    override fun insertOrder(vararg orderDTO: OrderDTO) {
        orderDTO.forEach {
            orderDTOS.add(it)
        }
    }

    override fun insertItem(vararg itemDTO: ItemDTO) {
        itemDTO.forEach {
            itemDTOS.add(it)
        }
    }

    override fun getOrders(): List<OrderDTO> {
        return orderDTOS
    }

    override fun getUnsyncedOrders(): List<OrderDTO> {
        TODO("Not yet implemented")
    }

    override fun updateOrderSync(orderId: String) {
        orderDTOS.find { it.id == orderId }?.synced = true
    }

    override fun getItems(): List<ItemDTO> {
        return itemDTOS
    }
}