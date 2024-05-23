package com.home.cdp2app.main.predict.usecase

import com.home.cdp2app.main.predict.repository.PredictRepository
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.home.cdp2app.user.token.usecase.GetAuthToken
import com.skydoves.sandwich.ApiResponse

/**
 * Predict를 수행하는 유스케이스
 * @param getAuthToken authToken을 가져오는 유스케이스 입니다. 해당 predict를 호출하기전 AuthToken의 존재 여부를 확인함으로 notnull입니다.
 * @param repository predict를 수행하는 레포지토리 입니다.
 */
class PredictUseCase(private val getAuthToken: GetAuthToken, private val repository : PredictRepository) {

    suspend operator fun invoke() : ApiResponse<PredictResponseDTO> {
        // TODO
        return repository.predict(getAuthToken(), "")
    }
}