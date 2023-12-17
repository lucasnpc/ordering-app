package com.example.orderingapp.main.di

import android.app.Application
import androidx.room.Room
import com.example.orderingapp.main.data.OrderingAppDatabase
import com.example.orderingapp.main.data.migrations.MIGRATION_1_2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideOrderingAppDB(app: Application): OrderingAppDatabase =
        Room.databaseBuilder(app, OrderingAppDatabase::class.java, OrderingAppDatabase.DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore
}