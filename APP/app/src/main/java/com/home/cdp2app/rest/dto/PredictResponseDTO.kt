package com.home.cdp2app.rest.dto

import com.home.cdp2app.predict.entity.PredictResult

/**
 * Predict 요청의 결과값을 나타냅니다.
 * @property percent 고혈압 확률을 나타냅니다.
 */
class PredictResponseDTO(private val percent : Double) {

    /**
     * 엔티티로 변환하는 함수
     * @return PredictResult 엔티티로 변환합니다.
     */
    fun toEntity() : PredictResult = PredictResult(percent)
}