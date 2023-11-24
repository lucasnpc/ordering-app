package com.example.orderingapp.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM Item")
    fun getItems(): List<Item>
}