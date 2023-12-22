package com.example.orderingapp.main.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ItemDTO ADD COLUMN costValue REAL NOT NULL DEFAULT 0.0")
    }
}
