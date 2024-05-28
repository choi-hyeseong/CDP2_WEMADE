package com.home.cdp2app.health.bloodpressure.usecase

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class LoadBloodPressureTest {

    private val repository : BloodPressureRepository = mockk() //for test mock
    private val loadBloodPressure = LoadBloodPressure(repository)

    @Test
    fun TEST_LOAD_BLOOD_PRESSURE() {
        //입력값과 출력값 같은지만 비교
        val inputDate = Instant.now()
        val captureDate : CapturingSlot<Instant> = slot()
        val returnValue = mutableListOf(BloodPressure(Instant.now(), 145.0, 50.0), BloodPressure(Instant.now(), 145.0, 50.0))
        coEvery { repository.readBloodPressureBefore(capture(captureDate)) } returns returnValue
        runBlocking {
            val result = loadBloodPressure(inputDate)
            assertEquals(inputDate, captureDate.captured)
            assertEquals(returnValue, result)
        }
    }
}