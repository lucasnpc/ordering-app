package com.example.orderingapp.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.orderingapp.main.data.entities.Item
import com.example.orderingapp.main.data.entities.Order

@Dao
interface OrderingAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(vararg order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(vararg item: Item)

    @Query("SELECT * FROM `Order`")
    fun getOrders(): List<Order>
    @Query("UPDATE `Order` SET synced = 1 WHERE id = :orderId")
    fun updateOrderSync(orderId: String)

    @Query("SELECT * FROM Item")
    fun getItems(): List<Item>
}