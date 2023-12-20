package com.example.orderingapp.main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO
import com.example.orderingapp.main.data.entities.PurchaseDTO

@Database(
    entities = [OrderDTO::class, ItemDTO::class, PurchaseDTO::class],
    version = 3,
    exportSchema = false
)
abstract class OrderingAppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "orderding_app_db"
    }

    abstract val orderingAppDao: OrderingAppDao
}