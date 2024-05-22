package com.home.cdp2app.predict.usecase

import com.home.cdp2app.main.predict.entity.PredictResult
import com.home.cdp2app.main.predict.repository.PredictCacheRepository
import com.home.cdp2app.main.predict.usecase.GetCachePredictResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class GetCachePredictResultTest {

    //유스케이스 레포지토리 호출하는지 확인
    private val reposiotry : PredictCacheRepository = mockk()
    private val getCachePredictResult = GetCachePredictResult(reposiotry)

    @Test
    fun TEST_GET_CACHE() {
        coEvery { reposiotry.getPredictResult() } returns PredictResult(12.3)

        runBlocking {
            val result = getCachePredictResult()
            coVerify(atLeast = 1) { reposiotry.getPredictResult() }
            assertNotNull(result)
            assertEquals(12.3, result!!.percent, 0.0)

        }
    }
}