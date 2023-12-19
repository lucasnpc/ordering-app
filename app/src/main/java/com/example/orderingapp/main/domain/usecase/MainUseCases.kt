package com.example.orderingapp.main.domain.usecase

data class MainUseCases(
    val getItemsUseCase: GetItemsUseCase,
    val insertItemsUseCase: InsertItemsUseCase,
    val getOrdersUseCase: GetOrdersUseCase,
    val insertOrderUseCase: InsertOrderUseCase,
    val syncOrderUseCase: SyncOrderUseCase,
    val updateItemsStockUseCase: UpdateItemsStockUseCase,
    val insertPurchaseUseCase: InsertPurchaseUseCase,
    val getPurchasesUseCase: GetPurchasesUseCase
)