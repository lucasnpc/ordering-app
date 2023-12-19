package com.example.orderingapp.main.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE new_OrderDTO (
                id TEXT PRIMARY KEY NOT NULL,
                items TEXT NOT NULL,
                date TEXT NOT NULL,
                hour TEXT NOT NULL,
                orderValue REAL NOT NULL,
                paymentWay TEXT NOT NULL,
                synced INTEGER NOT NULL
            )
        """.trimIndent())

        database.execSQL("""
            INSERT INTO new_OrderDTO (id, items, date, hour, orderValue, paymentWay, synced)
            SELECT id, items, date, hour, orderValue, paymentWay, synced FROM OrderDTO
        """.trimIndent())

        database.execSQL("DROP TABLE OrderDTO")

        database.execSQL("ALTER TABLE new_OrderDTO RENAME TO OrderDTO")
    }
}
