package com.home.cdp2app.main.predict.usecase

import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.home.cdp2app.main.predict.repository.PredictRepository
import com.home.cdp2app.main.setting.basicinfo.usecase.LoadBasicInfo
import com.home.cdp2app.user.token.usecase.GetAuthToken
import com.skydoves.sandwich.ApiResponse
import java.time.Instant

/**
 * Predict를 수행하는 유스케이스
 * @param getAuthToken authToken을 가져오는 유스케이스 입니다. 해당 predict를 호출하기전 AuthToken의 존재 여부를 확인함으로 notnull입니다.
 * @param repository predict를 수행하는 레포지토리 입니다.
 */
class PredictUseCase(private val getAuthToken: GetAuthToken, private val loadBasicInfo: LoadBasicInfo, private val loadSleepHour: LoadSleepHour, private val loadHeartRate: LoadHeartRate, private val loadBloodPressure: LoadBloodPressure, private val repository: PredictRepository) {

    suspend operator fun invoke(isExercised : Boolean): ApiResponse<PredictResponseDTO> {
        // TODO
        val instant = Instant.now()
        //제일 최신 값을 가져오고, 없을경우 null 입력
        return repository.predict(isExercised, getAuthToken(), loadBasicInfo(true), loadBloodPressure(instant).lastOrNull(), loadHeartRate(instant).lastOrNull(), loadSleepHour(instant).lastOrNull())
    }
}