package com.example.orderingapp.main.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `PurchaseDTO` (
                `id` TEXT NOT NULL,
                `items` TEXT NOT NULL,
                `date` TEXT NOT NULL,
                `hour` TEXT NOT NULL,
                `purchaseValue` REAL NOT NULL,
                `paymentWay` TEXT NOT NULL,
                `synced` INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY(`id`)
            )
        """.trimIndent())
    }
}