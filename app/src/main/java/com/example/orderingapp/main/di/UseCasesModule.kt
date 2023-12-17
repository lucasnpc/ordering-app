package com.example.orderingapp.main.di

import com.example.orderingapp.main.data.OrderingAppDatabase
import com.example.orderingapp.main.data.repositories.GetItemsRepository
import com.example.orderingapp.main.data.repositories.GetOrdersRepository
import com.example.orderingapp.main.data.repositories.InsertItemsRepository
import com.example.orderingapp.main.data.repositories.InsertOrderRepository
import com.example.orderingapp.main.data.repositories.InsertPurchaseRepository
import com.example.orderingapp.main.data.repositories.SyncOrderRepository
import com.example.orderingapp.main.data.repositories.UpdateItemsStockRepository
import com.example.orderingapp.main.domain.usecase.MainUseCases
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {
    @ViewModelScoped
    @Provides
    fun provideMainUseCase(orderingAppDatabase: OrderingAppDatabase, firestore: FirebaseFirestore) =
        MainUseCases(
            getItemsUseCase = GetItemsRepository(
                firestore = firestore,
                dao = orderingAppDatabase.orderingAppDao
            ),
            insertItemsUseCase = InsertItemsRepository(dao = orderingAppDatabase.orderingAppDao),
            getOrdersUseCase = GetOrdersRepository(dao = orderingAppDatabase.orderingAppDao),
            insertOrderUseCase = InsertOrderRepository(orderingAppDao = orderingAppDatabase.orderingAppDao),
            syncOrderUseCase = SyncOrderRepository(
                firestore = firestore,
                dao = orderingAppDatabase.orderingAppDao
            ),
            updateItemsStockUseCase = UpdateItemsStockRepository(firestore = firestore),
            insertPurchaseUseCase = InsertPurchaseRepository(orderingAppDao = orderingAppDatabase.orderingAppDao)
        )
}