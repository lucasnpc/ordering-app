package com.example.orderingapp.commons.extensions


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DoubleExtensionsKtTest {

    @Test
    fun roundDouble() {
        val double = 124.999999
        val doubleExpected = 125.0
        assertThat(double.roundDouble()).isEqualTo(doubleExpected)
    }
}