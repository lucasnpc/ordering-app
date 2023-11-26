package com.example.orderingapp.main.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.entities.ItemDTO
import com.example.orderingapp.main.data.entities.OrderDTO
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class OrderingAppDatabaseTest {
    private lateinit var database: OrderingAppDatabase
    private lateinit var dao: OrderingAppDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            OrderingAppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.orderingAppDao
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun getOrderingAppDao() {
        dao.insertOrder(orderDTO)
        assertThat(dao.getOrders()).isNotEmpty()
        assertThat(dao.getOrders()[0].items).containsKey(itemDTO.id)
    }

    @Test
    fun getItems() {
        dao.insertItem(itemDTO)
        assertThat(dao.getItems()).contains(itemDTO)
    }

    @Test
    fun updateOrder() {
        dao.insertOrder(orderDTO)
        assertThat(dao.getOrders()[0].synced).isFalse()
        dao.updateOrderSync(dao.getOrders()[0].id)
        assertThat(dao.getOrders()[0].synced).isTrue()
    }

    companion object {
        val itemDTO = ItemDTO(
            description = "Item Test",
            currentStock = 10,
            currentValue = 10.0,
            minimumStock = 1
        )
        val orderDTO = OrderDTO(
            items = mapOf(
                itemDTO.id to 10
            ), dateHour = "12:00:00 21/12/2021", orderValue = 100.0
        )
    }
}