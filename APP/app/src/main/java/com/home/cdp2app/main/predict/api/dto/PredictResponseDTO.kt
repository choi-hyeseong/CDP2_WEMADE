package com.home.cdp2app.main.predict.api.dto

import com.home.cdp2app.main.predict.entity.PredictResult

/**
 * Predict 요청의 결과값을 나타냅니다.
 * @property prediction_result 고혈압 확률을 나타냅니다. 0~1 사이
 */
data class PredictResponseDTO(private val prediction_result : Float) {

    /**
     * 엔티티로 변환하는 함수
     * @return PredictResult 엔티티로 변환합니다.
     */
    fun toEntity() : PredictResult = PredictResult(prediction_result * 100.0)
}