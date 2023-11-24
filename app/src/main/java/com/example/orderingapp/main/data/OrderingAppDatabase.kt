package com.example.orderingapp.main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.Item
import com.example.orderingapp.main.data.entities.Order

@Database(entities = [Order::class, Item::class], version = 1, exportSchema = false)
abstract class OrderingAppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "orderding_app_db"
    }

    abstract val orderingAppDao: OrderingAppDao
}