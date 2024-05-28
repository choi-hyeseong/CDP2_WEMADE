package com.home.cdp2app.health.heart.usecase

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.repository.HeartRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class SaveHeartRateTest {

    private val repository : HeartRepository = mockk() //for test mock
    private val saveHeartRate = SaveHeartRate(repository)

    @Test
    fun TEST_LOAD_HEART_RATE() {
        //입력값과 출력값 같은지만 비교
        val captureInput : CapturingSlot<List<HeartRate>> = slot()
        val saveValue = mutableListOf(HeartRate(Instant.now(), 145), HeartRate(Instant.now(), 140))
        coEvery { repository.writeHeartRate(capture(captureInput)) } returns mockk()
        runBlocking {
            val result = saveHeartRate(saveValue)
            coVerify(atLeast = 1) { repository.writeHeartRate(any()) }
            assertEquals(saveValue, captureInput.captured)
            assertEquals(2, captureInput.captured.size)
        }
    }
}