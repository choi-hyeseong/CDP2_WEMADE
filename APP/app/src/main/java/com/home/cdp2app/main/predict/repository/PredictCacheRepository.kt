package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.main.predict.entity.PredictResult

/**
 * 이전 Predict의 결과를 저장해두었다 앱 재시작시 불러올때 사용할 레포지토리 입니다.
 */
interface PredictCacheRepository {

    /**
     * PredictResult를 가져옵니다. hasResult 메소드를 안쓴 버젼으로 제작했습니다.
     * @return PredictResult가 저장되어 있지않은 경우 null을 리턴합니다.
     */
    suspend fun getPredictResult() : PredictResult?

    /**
     * PredictResult를 저장합니다.
     * @param predictResult 저장할 Result값입니다.
     * @return 저장의 성공 여부입니다.
     */
    suspend fun savePredictResult(predictResult: PredictResult) : Boolean
}