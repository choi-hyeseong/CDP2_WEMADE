package com.home.cdp2app.main.predict.api

import com.home.cdp2app.main.predict.api.dto.PredictRequestDTO
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 고혈압 예측 API
 */
interface PredictAPI {

    /**
     * 고혈압 예측 api를 호출합니다.
     * @param auth Authorization 헤더에 들어갈 jwt token값입니다. AuthToken.getHeaderAccessToken값을 넣으면 됩니다.
     * @param predictRequestDTO 예측에 사용될 요청 값입니다.
     * @return PredictResponse값이 반환됩니다. - 예측결과
     */
    @POST("/health/predict") //todo
    suspend fun predict(@Header("authorization") auth : String, @Body predictRequestDTO: PredictRequestDTO) : ApiResponse<PredictResponseDTO>
}