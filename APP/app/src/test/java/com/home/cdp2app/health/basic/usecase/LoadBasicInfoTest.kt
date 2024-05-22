package com.home.cdp2app.health.basic.usecase

import com.home.cdp2app.main.setting.basicinfo.repository.BasicInfoRepository
import com.home.cdp2app.main.setting.basicinfo.usecase.LoadBasicInfo
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class LoadBasicInfoTest {

    private val basicInfoRepository : BasicInfoRepository = mockk() //요청 테스트용 mock
    private val loadBasicInfo : LoadBasicInfo = LoadBasicInfo(basicInfoRepository)

    //true로 요청한경우
    @Test
    fun TEST_LOAD_DEFAULT_TRUE_BASIC_INFO() {
        val captureParam : CapturingSlot<Boolean> = slot() //입력값 테스트 위한 slot
        coEvery { basicInfoRepository.loadInfo(capture(captureParam)) } returns mockk() //load capture
        runBlocking {
            loadBasicInfo(true)
            assertTrue(captureParam.captured)
        }
    }

    //false로 요청한경우
    @Test
    fun TEST_LOAD_DEFAULT_FALSE_BASIC_INFO() {
        val captureParam : CapturingSlot<Boolean> = slot() //입력값 테스트 위한 slot
        coEvery { basicInfoRepository.loadInfo(capture(captureParam)) } returns mockk() //load capture
        runBlocking {
            loadBasicInfo(false)
            assertFalse(captureParam.captured)
        }
    }
}