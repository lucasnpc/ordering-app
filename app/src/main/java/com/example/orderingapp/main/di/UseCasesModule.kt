package com.example.orderingapp.main.di

import com.example.orderingapp.main.data.OrderingAppDatabase
import com.example.orderingapp.main.data.repositories.GetItemsRepository
import com.example.orderingapp.main.data.repositories.InsertItemsRepository
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
            getItemsUseCase = GetItemsRepository(firestore, orderingAppDatabase.orderingAppDao),
            insertItemsUseCase = InsertItemsRepository(orderingAppDatabase.orderingAppDao)
        )
}