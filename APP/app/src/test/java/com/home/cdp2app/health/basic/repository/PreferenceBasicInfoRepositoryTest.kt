package com.home.cdp2app.health.basic.repository

import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.health.basic.type.Gender
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.memory.exception.TargetNotFoundException
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class PreferenceBasicInfoRepositoryTest {

    private val preferencesStorage : SharedPreferencesStorage = mockk()
    private val preferenceBasicInfoRepository : PreferenceBasicInfoRepository = PreferenceBasicInfoRepository(preferencesStorage)

    @Test
    fun TEST_SAVE_INFO() {
        //입력할 값
        val basicInfo = BasicInfo(175.0, 70.0, Gender.MAN, true)

        //키값 캡처용 변수
        val captureKey : CapturingSlot<String> = slot()
        //info 캡처용 변수
        val captureSlot : CapturingSlot<BasicInfo> = slot()

        //값 저장시 캡처
        coEvery { preferencesStorage.saveObject(capture(captureKey), capture(captureSlot)) } returns mockk()
        runBlocking {
            //저장
            preferenceBasicInfoRepository.saveInfo(basicInfo)
            coVerify(atLeast = 1) { preferencesStorage.saveObject(any(), any()) } //저장이 수행됐는지
            assertEquals("BASIC_INFO", captureKey.captured) //키값 캡처
            assertEquals(basicInfo, captureSlot.captured) //value 캡처
        }
    }

    @Test
    fun TEST_LOAD_SUCCESS() {
        val basicInfo = BasicInfo(174.0, 75.0, Gender.WOMAN, false) //로드될 info
        coEvery { preferencesStorage.loadObject(any(), BasicInfo::class) } returns basicInfo //basic info 반환

        runBlocking {
            val result = preferenceBasicInfoRepository.loadInfo(true)
            assertEquals(basicInfo, result)
        }
    }

    @Test
    fun TEST_LOAD_DEFAULT() {
        coEvery { preferencesStorage.loadObject(any(), BasicInfo::class) } throws TargetNotFoundException("테스트용 Exception") //존재하지 않는 value이므로 throw

        runBlocking {
            val result = preferenceBasicInfoRepository.loadInfo(true)
            assertEquals(BasicInfoRepository.DEFAULT, result)
        }
    }

    @Test
    fun TEST_THROW_EXCEPTION_NO_SUCH() {
        //저장된 값이 없고, default 로드 안하면 exception 던지기
        val expectedThrow = TargetNotFoundException("존재하지 않는 값입니다.")
        coEvery { preferencesStorage.loadObject(any(), BasicInfo::class) } throws expectedThrow

        runBlocking {
            val result = assertThrows<TargetNotFoundException> { preferenceBasicInfoRepository.loadInfo(false) } //throw 되는지 확인
            assertEquals(expectedThrow, result) //exception 비교
        }
    }

    @Test
    fun TEST_THROW_EXCEPTION_ILLEAGL_ARG() {
        //일어날 가능성이 없지만, 만약 preference가 손상되어 역직렬화 안되더라도 throw 발생되는지 확인.
        val expectedThrow = IllegalArgumentException("역직렬화 할 수 없습니다.")
        coEvery { preferencesStorage.loadObject(any(), BasicInfo::class) } throws expectedThrow

        runBlocking {
            val result = assertThrows<IllegalArgumentException> { preferenceBasicInfoRepository.loadInfo(false) } //throw 되는지 확인
            assertEquals(expectedThrow, result) //exception 비교
        }
    }

    @Test
    fun TEST_HAVE_INFO() {
        coEvery { preferencesStorage.loadObject(any(), BasicInfo::class) } returns BasicInfo(180.0, 80.0, Gender.MAN, false) //DEFAULT와 값은 동일하지만, 다른 객체 (저장된 객체)

        runBlocking {
            assertTrue(preferenceBasicInfoRepository.hasInfo())
        }
    }

    @Test
    fun TEST_NOT_HAVE_INFO() {
        coEvery { preferencesStorage.loadObject(any(), BasicInfo::class) } throws TargetNotFoundException("아무튼 exception")

        runBlocking {
            assertFalse(preferenceBasicInfoRepository.hasInfo())
        }
    }

}