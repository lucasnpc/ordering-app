package com.example.orderingapp.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.data.entities.PurchaseDTO

@Dao
interface OrderingAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(vararg orderDTO: OrderDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(vararg itemDTO: ItemDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPurchase(vararg purchaseDTO: PurchaseDTO)

    @Query("SELECT * FROM OrderDTO")
    fun getOrders(): List<OrderDTO>

    @Query("SELECT * FROM OrderDTO WHERE synced == 0")
    fun getUnsyncedOrders(): List<OrderDTO>

    @Query("UPDATE OrderDTO SET synced = 1 WHERE id = :orderId")
    fun updateOrderSync(orderId: String)

    @Query("SELECT * FROM ItemDTO")
    fun getItems(): List<ItemDTO>
}