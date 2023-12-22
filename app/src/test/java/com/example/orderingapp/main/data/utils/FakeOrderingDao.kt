package com.example.orderingapp.main.data.utils

import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.data.entities.PurchaseDTO

class FakeOrderingDao : OrderingAppDao {
    private val itemDTOS = arrayListOf<ItemDTO>()
    private val orderDTOS = arrayListOf<OrderDTO>()
    private val purchaseDTOS = arrayListOf<PurchaseDTO>()
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

    override fun insertPurchase(vararg purchaseDTO: PurchaseDTO) {
        purchaseDTO.forEach {
            purchaseDTOS.add(it)
        }
    }

    override fun getOrders(): List<OrderDTO> {
        return orderDTOS
    }

    override fun getUnsyncedOrders(): List<OrderDTO> {
        return orderDTOS.filter { !it.synced }
    }

    override fun getUnsyncedPurchases(): List<PurchaseDTO> {
        return purchaseDTOS.filter { !it.synced }
    }

    override fun updateOrderSync(orderId: String) {
        orderDTOS.find { it.id == orderId }?.synced = true
    }

    override fun updatePurchaseSync(purchaseId: String) {
        purchaseDTOS.find { it.id == purchaseId }?.synced = true
    }

    override fun getItems(): List<ItemDTO> {
        return itemDTOS
    }

    override fun deleteOrder(order: OrderDTO) {
        orderDTOS.remove(order)
    }
}