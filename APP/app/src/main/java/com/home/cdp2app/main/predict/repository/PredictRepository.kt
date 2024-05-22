package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.rest.dto.PredictResponseDTO
import com.home.cdp2app.user.token.entity.AuthToken
import com.skydoves.sandwich.ApiResponse

/**
 * 고혈압 예측을 수행하는 레포지토리
 */
interface PredictRepository {

    /**
     * 고혈압 예측을 수행합니다.
     */
    // todo parameter
    suspend fun predict(authToken: AuthToken, health : String) : ApiResponse<PredictResponseDTO>
}