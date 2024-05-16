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
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class HasBasicInfoTest {

    private val repository : BasicInfoRepository = mockk()
    private val hasBasicInfo = HasBasicInfo(repository)

    @Test
    fun TEST_CALL_HAVE_INFO() {
        // repository 호출만 확인. 실제 구현체를 명시(PreferenceBasicInfo..)한것이 아니기 때문에, 내부 구현 (loadInfo !==)등의 비교는 수행하지 않음
        coEvery { repository.hasInfo() } returns false
        runBlocking {
            val hasInfo = hasBasicInfo()
            coVerify(atLeast = 1) { repository.hasInfo() } // 호출됐는지 확인
            assertFalse(hasInfo)
        }
    }
}