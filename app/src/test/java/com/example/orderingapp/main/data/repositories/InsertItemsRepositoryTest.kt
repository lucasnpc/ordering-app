package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
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
    private val list = TestData().items
    private val listDTO = TestData().itemsDTO

    @Before
    fun setUp() {
        dao = FakeOrderingDao()
        insertItemsUseCase = InsertItemsRepository(dao)
    }

    @Test
    fun insertItem() = runTest {
        insertItemsUseCase.insertItem(list).take(1).collect {
            assertThat(it).isInstanceOf(ApiResult.Success::class.java)
            assertThat(dao.getItems()).isEqualTo(listDTO.values.toList())
        }
    }

    @Test
    fun insertItemException() = runTest {
        dao = mockk()
        insertItemsUseCase = InsertItemsRepository(dao)
        every { dao.insertItem(listDTO.values.first()) } throws testException
        insertItemsUseCase.insertItem(list).take(1).collect {
            assertThat(it).isInstanceOf(ApiResult.Error::class.java)
            it as ApiResult.Error
            assertThat(it.exception.message).isEqualTo(testMsgException)
        }
    }
}