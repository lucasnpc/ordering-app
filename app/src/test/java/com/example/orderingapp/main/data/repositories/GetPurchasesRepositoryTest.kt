package com.example.orderingapp.main.data.repositories

import com.example.orderingapp.commons.request.ApiResult
import com.example.orderingapp.main.commons.TestConstants.testException
import com.example.orderingapp.main.commons.TestConstants.testMsgException
import com.example.orderingapp.main.commons.TestData
import com.example.orderingapp.main.data.dao.OrderingAppDao
import com.example.orderingapp.main.domain.usecase.GetPurchasesUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPurchasesRepositoryTest {
    private lateinit var getPurchasesUseCase: GetPurchasesUseCase
    private val dao: OrderingAppDao = mockk()
    private val purchasesDTO = TestData().purchasesDTO
    private val listPurchases = TestData().purchases

    @Before
    fun setUp() {
        getPurchasesUseCase = GetPurchasesRepository(dao)
    }

    @Test
    fun getUnsyncedPurchases() = runTest {
        every { dao.getUnsyncedPurchases() } returns purchasesDTO
        getPurchasesUseCase.getUnsyncedPurchases().collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Success::class.java)
            result as ApiResult.Success
            assertThat(result.data).isEqualTo(listPurchases)
        }
    }

    @Test
    fun getUnsyncedPurchasesException() = runTest {
        every { dao.getUnsyncedPurchases() } throws testException
        getPurchasesUseCase.getUnsyncedPurchases().collect { result ->
            assertThat(result).isInstanceOf(ApiResult.Error::class.java)
            result as ApiResult.Error
            assertThat(result.exception.message).isEqualTo(testMsgException)
        }
    }
}