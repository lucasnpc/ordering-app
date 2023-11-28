package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.ApiResult
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.commons.TestConstants.item
import com.example.orderingapp.main.commons.TestConstants.itemDTO
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.domain.usecase.InsertItemsUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class InsertItemsRepositoryTest {
    private lateinit var insertItemsUseCase: InsertItemsUseCase
    private lateinit var dao: OrderingAppDao

    @Before
    fun setUp() {
        dao = FakeOrderingDao()
        insertItemsUseCase = InsertItemsRepository(dao)
    }

    @Test
    fun insertItem() = runTest {
        insertItemsUseCase.insertItem(listOf(item)).take(1).collect {
            assertThat(it).isInstanceOf(ApiResult.Success::class.java)
            assertThat(dao.getItems()).contains(itemDTO)
        }
    }

    @Test
    fun insertItemException() = runTest {
        dao = mockk()
        insertItemsUseCase = InsertItemsRepository(dao)
        every { dao.insertItem(itemDTO) } throws testException
        insertItemsUseCase.insertItem(listOf(item)).take(1).collect {
            assertThat(it).isInstanceOf(ApiResult.Error::class.java)
            it as ApiResult.Error
            assertThat(it.exception.message).isEqualTo(testMsgException)
        }
    }
}