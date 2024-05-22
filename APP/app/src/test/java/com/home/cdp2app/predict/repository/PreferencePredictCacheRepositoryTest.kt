package com.home.cdp2app.predict.repository

import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.main.predict.entity.PredictResult
import com.home.cdp2app.main.predict.repository.PreferencePredictCacheRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class PreferencePredictCacheRepositoryTest {

    private val storage : SharedPreferencesStorage = mockk() //storage mock
    private val repository = PreferencePredictCacheRepository(storage)

    @Test
    fun TEST_LOAD_FAIL() {
        // exception이 발생해 PredictResult를 못가져옴
        val key : CapturingSlot<String> = slot()
        coEvery { storage.loadObject(capture(key), PredictResult::class) } throws IllegalArgumentException("읎어요")

        runBlocking {
            assertNull(repository.getPredictResult())
            assertEquals("PREDICT_KEY", key.captured)
        }
    }

    @Test
    fun TEST_LOAD_SUCCESS() {
        val response = PredictResult(50.0)
        coEvery { storage.loadObject(any(), PredictResult::class) } returns response

        runBlocking {
            val result = repository.getPredictResult()
            assertNotNull(result)
            assertEquals(response, result)
        }
    }

    @Test
    fun TEST_SAVE_PREDICT() {
        val value : CapturingSlot<PredictResult> = slot()
        coEvery { storage.saveObject(any(), capture(value)) } returns mockk()

        runBlocking {
            repository.savePredictResult(PredictResult(45.5))
            coVerify(atLeast = 1) { storage.saveObject(any(), any()) }
            assertEquals(45.5, value.captured.percent, 0.0)
        }
    }
}