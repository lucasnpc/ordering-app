package com.example.orderingapp.main.data.utils

import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO

class FakeOrderingDao : OrderingAppDao {
    private val itemDTOS = arrayListOf<ItemDTO>()
    override fun insertOrder(vararg orderDTO: OrderDTO) {
        TODO("Not yet implemented")
    }

    override fun insertItem(vararg itemDTO: ItemDTO) {
        itemDTO.forEach {
            itemDTOS.add(it)
        }
    }

    override fun getOrders(): List<OrderDTO> {
        TODO("Not yet implemented")
    }

    override fun getUnsyncedOrders(): List<OrderDTO> {
        TODO("Not yet implemented")
    }

    override fun updateOrderSync(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<ItemDTO> {
        return itemDTOS
    }
}