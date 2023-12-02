package com.example.orderingapp.commons.extensions


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyExtensionsKtTest {

    @Test
    fun currencyFormat() {
        val double = 125.0
        val doubleCurrency = "R$ 125,00"
        assertThat(double.currencyFormat()).isEqualTo(doubleCurrency)
    }
}