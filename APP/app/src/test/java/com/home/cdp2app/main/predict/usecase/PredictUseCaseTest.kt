package com.home.cdp2app.main.predict.usecase

import android.graphics.Paint.Cap
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.main.predict.repository.RemotePredictRepository
import com.home.cdp2app.main.setting.basicinfo.entity.BasicInfo
import com.home.cdp2app.main.setting.basicinfo.repository.BasicInfoRepository
import com.home.cdp2app.main.setting.basicinfo.usecase.LoadBasicInfo
import com.home.cdp2app.user.token.entity.AuthToken
import com.home.cdp2app.user.token.usecase.GetAuthToken
import com.skydoves.sandwich.ApiResponse
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Response
import java.time.Duration
import java.time.Instant

class PredictUseCaseTest {

    private val getAuthToken : GetAuthToken = mockk()
    private val loadBasicInfo : LoadBasicInfo = mockk()
    private val repository : RemotePredictRepository = mockk()
    private val loadBloodPressure : LoadBloodPressure = mockk()
    private val loadSleepHour : LoadSleepHour = mockk()
    private val predictUseCase = PredictUseCase(getAuthToken, loadBasicInfo, loadSleepHour, loadBloodPressure, repository)

    @Test
    fun TEST_GET_NULL() {
        // usecase에서 비어있는경우 null 가져오는지 확인
        val authToken = AuthToken("ACCESS", "REFRESH")
        val basicInfo = BasicInfoRepository.DEFAULT

        //for capture
        val isExercise : CapturingSlot<Boolean> = slot()
        val captureToken : CapturingSlot<AuthToken> = slot()
        val captureInfo : CapturingSlot<BasicInfo> = slot()
        val captureSleep : CapturingSlot<SleepHour?> = slot() //nullable capture로 따로 해야됨
        val captureBlood : CapturingSlot<BloodPressure?> = slot()

        coEvery { getAuthToken() } returns authToken
        coEvery { loadBasicInfo(true) } returns basicInfo
        //둘다 빈 리스트 입력
        coEvery { loadBloodPressure(any()) } returns mutableListOf()
        coEvery { loadSleepHour(any()) } returns mutableListOf()

        coEvery { repository.predict(capture(isExercise), capture(captureToken), capture(captureInfo), captureNullable(captureBlood), captureNullable(captureSleep)) } returns ApiResponse.of { Response.success(mockk()) } //요청 들어가는지만 확인

        runBlocking {
            predictUseCase(false)
            coVerify(atLeast = 1) { repository.predict(any(), any(), any(), any(), any()) }
            assertFalse(isExercise.captured)
            assertEquals(authToken, captureToken.captured)
            assertEquals(basicInfo, captureInfo.captured)
            // null로 입력되는지 확인
            assertNull(captureBlood.captured)
            assertNull(captureSleep.captured)
        }

    }

    @Test
    fun TEST_GET_NOT_NULL() {
        // usecase에서 비어있는경우 null 가져오는지 확인
        val authToken = AuthToken("ACCESS", "REFRESH")
        val basicInfo = BasicInfoRepository.DEFAULT

        //for capture
        val isExercise : CapturingSlot<Boolean> = slot()
        val captureToken : CapturingSlot<AuthToken> = slot()
        val captureInfo : CapturingSlot<BasicInfo> = slot()
        val captureSleep : CapturingSlot<SleepHour?> = slot() //nullable capture로 따로 해야됨
        val captureBlood : CapturingSlot<BloodPressure?> = slot()

        coEvery { getAuthToken() } returns authToken
        coEvery { loadBasicInfo(true) } returns basicInfo
        //둘다 빈 리스트 입력
        coEvery { loadBloodPressure(any()) } returns listOf(BloodPressure(Instant.now(), 120.0, 80.0))
        coEvery { loadSleepHour(any()) } returns listOf(SleepHour(Instant.now(), Duration.ofHours(1)))

        coEvery { repository.predict(capture(isExercise), capture(captureToken), capture(captureInfo), captureNullable(captureBlood), captureNullable(captureSleep)) } returns ApiResponse.of { Response.success(mockk()) } //요청 들어가는지만 확인

        runBlocking {
            predictUseCase(false)
            coVerify(atLeast = 1) { repository.predict(any(), any(), any(), any(), any()) }
            assertFalse(isExercise.captured)
            assertEquals(authToken, captureToken.captured)
            assertEquals(basicInfo, captureInfo.captured)
            // 일치하는지 확인
            assertEquals(120.0, captureBlood.captured?.systolic ?: 0.0, 0.0)
            assertEquals(60, captureSleep.captured?.duration?.toMinutes() ?: 0)
        }

    }


}