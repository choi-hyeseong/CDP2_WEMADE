package com.home.cdp2app.main.predict.usecase

import com.home.cdp2app.main.predict.entity.PredictResult
import com.home.cdp2app.main.predict.repository.PredictCacheRepository

/**
 * Predict 결과를 저장하는 유스케이스
 * @property repository 저장에 사용되는 레포지토리 입니다.
 */
class SaveCachePredictResult(private val repository: PredictCacheRepository) {

    /**
     * Predict 결과를 저장합니다.
     * @param predictResult 저장할 결과값입니다.
     * @return 저장의 성공 여부입니다.
     * @see PredictCacheRepository.savePredictResult
     */
    suspend operator fun invoke(predictResult: PredictResult) : Boolean {
        return repository.savePredictResult(predictResult)
    }
}