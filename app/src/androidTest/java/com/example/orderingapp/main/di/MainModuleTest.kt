package com.example.orderingapp.main.di

import com.example.orderingapp.main.data.OrderingAppDatabase
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class MainModuleTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var orderingAppDatabase: OrderingAppDatabase

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun provideOrderingAppDB() {
        assertThat(orderingAppDatabase).isNotNull()
    }

    @Test
    fun provideFirestore() {
        assertThat(firestore).isNotNull()
    }
}