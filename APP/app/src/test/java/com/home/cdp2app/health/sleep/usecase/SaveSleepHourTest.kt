package com.home.cdp2app.health.sleep.usecase

import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.repository.SleepRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.Instant

class SaveSleepHourTest {
    private val repository : SleepRepository = mockk() //for test mock
    private val saveSleepHour = SaveSleepHour(repository)

    @Test
    fun TEST_LOAD_BLOOD_PRESSURE() {
        //입력값과 출력값 같은지만 비교
        val captureValue : CapturingSlot<List<SleepHour>> = slot()
        val saveValue = mutableListOf(SleepHour(Instant.now(), Duration.ofSeconds(60)))
        coEvery { repository.writeSleepHours(capture(captureValue)) } returns mockk()
        runBlocking {
            saveSleepHour(saveValue)
            coVerify(atLeast = 1) { repository.writeSleepHours(any()) }
            assertEquals(1, captureValue.captured.size)
            assertEquals(saveValue, captureValue.captured)
        }
    }
}