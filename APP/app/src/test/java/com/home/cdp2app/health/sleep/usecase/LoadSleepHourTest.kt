package com.home.cdp2app.health.sleep.usecase

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.repository.SleepRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.Instant

class LoadSleepHourTest {

    private val repository : SleepRepository = mockk() //for test mock
    private val loadSleepHour = LoadSleepHour(repository)

    @Test
    fun TEST_LOAD_BLOOD_PRESSURE() {
        //입력값과 출력값 같은지만 비교
        val inputDate = Instant.now()
        val captureDate : CapturingSlot<Instant> = slot()
        val returnValue = mutableListOf(SleepHour(Instant.now(), Duration.ofSeconds(60)))
        coEvery { repository.readSleepHoursBefore(capture(captureDate)) } returns returnValue
        runBlocking {
            val result = loadSleepHour(inputDate)
            assertEquals(inputDate, captureDate.captured)
            assertEquals(returnValue, result)
        }
    }
}