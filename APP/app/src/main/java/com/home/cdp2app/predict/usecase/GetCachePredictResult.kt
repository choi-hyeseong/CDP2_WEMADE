package com.home.cdp2app.predict.usecase

import com.home.cdp2app.predict.entity.PredictResult
import com.home.cdp2app.predict.repository.PredictCacheRepository

/**
 * 캐시 predict 가져오기
 * @property predictCacheRepository 불러오기에 사용되는 레포지토리 입니다.
 */
class GetCachePredictResult(private val predictCacheRepository: PredictCacheRepository) {

    /**
     * 실제 수행하는 메소드
     * @return 저장된 PredictResult (이전의 결과)를 리턴합니다.
     * @see PredictCacheRepository.getPredictResult
     */
    suspend operator fun invoke() : PredictResult? {
        return predictCacheRepository.getPredictResult()
    }
}