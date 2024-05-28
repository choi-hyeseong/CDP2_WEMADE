package com.home.cdp2app.health.bloodpressure.usecase

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class SaveBloodPressureTest {
    private val repository : BloodPressureRepository = mockk() //for test mock
    private val saveBloodPressure = SaveBloodPressure(repository)

    @Test
    fun TEST_SAVE_BLOOD_PRESSURE() {
        //입력값과 출력값 같은지만 비교
        val capturePressure : CapturingSlot<BloodPressure> = slot()
        val saveValue = BloodPressure(Instant.now(), 145.0, 50.0)
        coEvery { repository.writeBloodPressure(capture(capturePressure)) } returns mockk()
        runBlocking {
            saveBloodPressure(saveValue)
            assertEquals(saveValue, capturePressure.captured)
            coVerify(atLeast = 1) { repository.writeBloodPressure(any()) } //호출 되었는지 확인
        }
    }
}