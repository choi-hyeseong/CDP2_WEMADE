package com.home.cdp2app.health.heart.usecase

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.repository.HeartRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class LoadHeartRateTest {

    private val repository : HeartRepository = mockk() //for test mock
    private val loadHeartRate = LoadHeartRate(repository)

    @Test
    fun TEST_LOAD_HEART_RATE() {
        //입력값과 출력값 같은지만 비교
        val inputDate = Instant.now()
        val captureDate : CapturingSlot<Instant> = slot()
        val returnValue = mutableListOf(HeartRate(Instant.now(), 145), HeartRate(Instant.now(), 140))
        coEvery { repository.readHeartRateBefore(capture(captureDate)) } returns returnValue
        runBlocking {
            val result = loadHeartRate(inputDate)
            assertEquals(inputDate, captureDate.captured)
            assertEquals(returnValue, result)
        }
    }
}