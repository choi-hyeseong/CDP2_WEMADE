package com.home.cdp2app.health.basic.usecase

import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.health.basic.repository.BasicInfoRepository
import com.home.cdp2app.health.basic.type.Gender
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test


class SaveBasicInfoTest {

    private val basicInfoRepository : BasicInfoRepository = mockk() //메소드 호출 확인용 mock
    private val saveBasicInfo = SaveBasicInfo(basicInfoRepository)

    @Test
    fun TEST_CALL_SAVE_INFO() {
        val info = BasicInfo(170.0, 70.0, 20, Gender.WOMAN, false)
        val slot : CapturingSlot<BasicInfo> = slot() //입력값 검증위한 슬롯

        coEvery { basicInfoRepository.saveInfo(capture(slot)) } returns mockk() //input capture
        runBlocking {
            saveBasicInfo(info)
            coVerify(atLeast = 1) { basicInfoRepository.saveInfo(any()) } // 호출됐는지 확인
            assertEquals(info, slot.captured)
        }
    }
}