package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.common.util.primitive.toInt
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.main.predict.api.PredictAPI
import com.home.cdp2app.main.predict.api.dto.PredictRequestDTO
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.home.cdp2app.main.setting.basicinfo.entity.BasicInfo
import com.home.cdp2app.user.token.entity.AuthToken
import com.skydoves.sandwich.ApiResponse
import java.text.DecimalFormat

// predict api를 이용한 repository
class RemotePredictRepository(private val predictAPI: PredictAPI) : PredictRepository {

    private val decimalFormat = DecimalFormat("#.#")

    // dto로 변환후 예측 수행
    override suspend fun predict(isExercised: Boolean, authToken: AuthToken, basicInfo: BasicInfo, latestBloodPressure: BloodPressure?, latestHeartRate: HeartRate?, latestSleepHour: SleepHour?): ApiResponse<PredictResponseDTO> {
        val sex = basicInfo.gender.toIntValue()
        val age = basicInfo.age
        val systolic = latestBloodPressure?.systolic?.toFloat()
        val diastolic = latestBloodPressure?.diastolic?.toFloat()
        val bmi = basicInfo.calculateBMI()
        val heartRate = latestHeartRate?.bpm?.toFloat()
        val smoking = basicInfo.isSmoking.toInt()//흡연하는경우 1 아닌경우 0
        val walk = isExercised.toInt()
        // 소수점 한자리 포맷하기 위해 null이 아닌경우 가져와서 format 수행, null일경우 null로 반환
        val totalSleep = if (latestSleepHour != null)
            decimalFormat.format(latestSleepHour.duration.toMinutes().div(60.0)).toFloat()
        else
            null

        return predictAPI.predict(authToken.getHeaderAccessToken(), PredictRequestDTO(sex, age, systolic, diastolic, bmi, heartRate, smoking, walk, totalSleep))
    }
}