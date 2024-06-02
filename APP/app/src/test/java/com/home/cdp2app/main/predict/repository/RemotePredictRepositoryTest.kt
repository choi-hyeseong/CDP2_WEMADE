package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.common.util.primitive.toInt
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.main.predict.api.PredictAPI
import com.home.cdp2app.main.predict.api.dto.PredictRequestDTO
import com.home.cdp2app.main.setting.basicinfo.entity.BasicInfo
import com.home.cdp2app.main.setting.basicinfo.repository.BasicInfoRepository
import com.home.cdp2app.user.token.entity.AuthToken
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

class RemotePredictRepositoryTest {

    private val api : PredictAPI = mockk()
    private val repository : RemotePredictRepository = RemotePredictRepository(api)

    @Test
    fun TEST_PREDICT_NULL() {
        // null 입력값에 대해 null로 제공된경우 dto에도 null로 표현하는지 확인
        val captureAuth : CapturingSlot<String> = slot() //auth token capture
        val dtoCapture : CapturingSlot<PredictRequestDTO> = slot() //dto capture
        coEvery { api.predict(capture(captureAuth), capture(dtoCapture)) } returns ApiResponse.of { Response.success(mockk()) } //capture & success

        val isExercise = false
        val authToken = AuthToken("ACCESS", "REFRESH")
        val basicInfo = BasicInfoRepository.DEFAULT

        runBlocking {
            repository.predict(isExercise, authToken, basicInfo, null, null)
            coVerify(atLeast = 1) { api.predict(any(), any()) } //predict 호출했는지 확인

            assertEquals(authToken.getHeaderAccessToken(), captureAuth.captured) //header 확인
            val dto = dtoCapture.captured
            assertEquals(isExercise.toInt(), dto.pa_walk)
            assertEquals(basicInfo.gender.toIntValue(), dto.sex)
            assertEquals(basicInfo.age, dto.age)
            assertEquals(basicInfo.calculateBMI(), dto.HE_BMI)
            assertNull(dto.HE_dbp)
            assertNull(dto.HE_sbp)
            assertNull(dto.total_sleep)
            assertEquals(basicInfo.isSmoking.toInt(), dto.sm_present)
        }
    }

    @Test
    fun TEST_PREDICT_NORMAL() {
        // 일반적인 경우의 predict
        val captureAuth : CapturingSlot<String> = slot() //auth token capture
        val dtoCapture : CapturingSlot<PredictRequestDTO> = slot() //dto capture
        coEvery { api.predict(capture(captureAuth), capture(dtoCapture)) } returns ApiResponse.of { Response.success(mockk()) } //capture & success

        val isExercise = false
        val authToken = AuthToken("ACCESS", "REFRESH")
        val basicInfo = BasicInfoRepository.DEFAULT
        val bloodPressure = BloodPressure(Instant.now(), 120.0, 70.0) //120-70 혈압 데이터
        val sleepHour = SleepHour(Instant.now(), Duration.ofHours(1)) //수면시간 (분단위 표현 필요)

        runBlocking {
            repository.predict(isExercise, authToken, basicInfo, bloodPressure, sleepHour)
            coVerify(atLeast = 1) { api.predict(any(), any()) } //predict 호출했는지 확인

            assertEquals(authToken.getHeaderAccessToken(), captureAuth.captured) //header 확인
            val dto = dtoCapture.captured
            assertEquals(isExercise.toInt(), dto.pa_walk)
            assertEquals(basicInfo.gender.toIntValue(), dto.sex)
            assertEquals(basicInfo.age, dto.age)
            assertEquals(basicInfo.calculateBMI(), dto.HE_BMI)
            assertEquals(bloodPressure.systolic, dto.HE_sbp?.toDouble() ?: 0.0, 0.0) //safe compare
            assertEquals(bloodPressure.diastolic, dto.HE_dbp?.toDouble() ?: 0.0, 0.0)
            assertEquals(sleepHour.duration.toMinutes() / 60.0, dto.total_sleep?.toDouble() ?: 0.0, 0.0)
            assertEquals(basicInfo.isSmoking.toInt(), dto.sm_present)
        }
    }
}