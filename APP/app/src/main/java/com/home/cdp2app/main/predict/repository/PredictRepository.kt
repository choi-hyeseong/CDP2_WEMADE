package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.home.cdp2app.main.setting.basicinfo.entity.BasicInfo
import com.home.cdp2app.user.token.entity.AuthToken
import com.skydoves.sandwich.ApiResponse

/**
 * 고혈압 예측을 수행하는 레포지토리
 */
interface PredictRepository {

    /**
     * 고혈압 예측을 수행합니다.
     * @param isExercised 운동 여부입니다.
     * @param authToken Header 인증에 필요한 token 정보입니다.
     * @param basicInfo 키, 몸무게 등 기본 건강정보 값입니다.
     * @param latestBloodPressure 제일 최근 혈압 측정값입니다. 저장되어 있지 않을 수 있어 nullable 합니다.
     * @param latestHeartRate 제일 최근 심박수 측정값입니다. 저장되어 있지 않을 수 있어 nullable 합니다.
     * @param latestSleepHour 제일 최근 수면시간입니다. 저장되어 있지 않을 수 있어 nullable 합니다.
     */
    suspend fun predict(isExercised : Boolean, authToken: AuthToken, basicInfo: BasicInfo, latestBloodPressure: BloodPressure?, latestHeartRate: HeartRate?, latestSleepHour: SleepHour?) : ApiResponse<PredictResponseDTO>
}