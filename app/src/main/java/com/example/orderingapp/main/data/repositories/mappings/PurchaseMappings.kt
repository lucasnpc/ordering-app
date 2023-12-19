package com.example.orderingapp.main.data.repositories.mappings

import com.example.orderingapp.main.data.entities.PurchaseDTO
import com.example.orderingapp.main.domain.model.Purchase

fun List<PurchaseDTO>.fromPurchaseDTOListToOrderMap(): Map<String, Purchase> {
    return this.associateBy({ it.id }, {
        Purchase(
            items = it.items,
            date = it.date,
            hour = it.hour,
            purchaseValue = it.purchaseValue,
            paymentWay = it.paymentWay,
        )
    })
}

fun Purchase.toPurchaseDTO(): PurchaseDTO {
    return PurchaseDTO(
        items = items,
        date = date,
        hour = hour,
        purchaseValue = purchaseValue,
        paymentWay = paymentWay,
    )
}