package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.main.predict.api.PredictAPI
import com.home.cdp2app.main.predict.api.dto.PredictRequestDTO
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.home.cdp2app.user.token.entity.AuthToken
import com.skydoves.sandwich.ApiResponse

// predict api를 이용한 repository
class RemotePredictRepository(private val predictAPI: PredictAPI) : PredictRepository {
    override suspend fun predict(authToken: AuthToken, health : String): ApiResponse<PredictResponseDTO> {
        return predictAPI.predict(authToken.getHeaderAccessToken(), PredictRequestDTO())
    }
}