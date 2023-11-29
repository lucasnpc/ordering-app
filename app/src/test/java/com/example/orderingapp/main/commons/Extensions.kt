package com.example.orderingapp.main.commons

import com.example.orderingapp.main.domain.model.Item
import com.google.common.truth.Truth

fun List<Item>.assertListItemEqualsTo(other: List<Item>) {
    for (i in this.indices) {
        Truth.assertThat(this[i].id).isEqualTo(other[i].id)
        Truth.assertThat(this[i].description).isEqualTo(other[i].description)
        Truth.assertThat(this[i].currentValue).isEqualTo(other[i].currentValue)
        Truth.assertThat(this[i].minimumStock).isEqualTo(other[i].minimumStock)
        Truth.assertThat(this[i].currentStock).isEqualTo(other[i].currentStock)
    }
}