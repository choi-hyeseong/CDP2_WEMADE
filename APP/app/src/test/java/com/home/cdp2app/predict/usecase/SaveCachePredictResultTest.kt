package com.home.cdp2app.predict.usecase

import com.home.cdp2app.predict.entity.PredictResult
import com.home.cdp2app.predict.repository.PredictCacheRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SaveCachePredictResultTest {

    //유스케이스 레포지토리 호출하는지 확인
    private val reposiotry : PredictCacheRepository = mockk()
    private val saveCachePredictResult = SaveCachePredictResult(reposiotry)

    @Test
    fun TEST_GET_CACHE() {
        val captureValue : CapturingSlot<PredictResult> = slot()
        coEvery { reposiotry.savePredictResult(capture(captureValue)) } returns mockk()

        runBlocking {
            saveCachePredictResult(PredictResult(45.5))
            coVerify(atLeast = 1) { reposiotry.savePredictResult(any()) }
            assertEquals(45.5, captureValue.captured.percent, 0.0)

        }
    }
}