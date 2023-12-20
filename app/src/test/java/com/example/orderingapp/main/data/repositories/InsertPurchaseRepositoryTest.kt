package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.data.utils.FakeOrderingDao
import com.example.orderingapp.main.domain.usecase.InsertPurchaseUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class InsertPurchaseRepositoryTest {
    private lateinit var insertPurchaseUseCase: InsertPurchaseUseCase
    private var dao: OrderingAppDao = FakeOrderingDao()
    private val listPurchase = TestData().purchases


    @Before
    fun setUp() {
        insertPurchaseUseCase = InsertPurchaseRepository(dao)
    }

    @Test
    fun insertPurchaseLocal() = runTest {
        listPurchase.forEach { purchase ->
            insertPurchaseUseCase.insertPurchaseLocal(purchase.value).collect { result ->
                assertThat(result).isInstanceOf(ApiResult.Success::class.java)
                result as ApiResult.Success
                assertThat(result.data.value).isEqualTo(purchase.value)
            }
        }
    }

    @Test
    fun insertPurchaseLocalException() = runTest {
        dao = mockk()
        insertPurchaseUseCase = InsertPurchaseRepository(dao)
        every { dao.insertPurchase(any()) } throws TestConstants.testException
        insertPurchaseUseCase.insertPurchaseLocal(listPurchase.values.first()).collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }
}