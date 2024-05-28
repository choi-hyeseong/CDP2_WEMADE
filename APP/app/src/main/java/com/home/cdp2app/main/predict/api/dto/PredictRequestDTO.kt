package com.home.cdp2app.main.predict.api.dto

/**
 * 예측 요청에 필요한 Request DTO입니다.
 * @property sex 성별입니다. (1 : 남성, 2 : 여성)
 * @property age 나이입니다.
 * @property HE_sbp 수축기 혈압입니다.
 * @property HE_dbp 이완기 혈압입니다.
 * @property HE_BMI 체질량 지수입니다.
 * @property HE_PLS 15초 맥박수 입니다.
 * @property sm_present 흡연 여부입니다.
 * @property pa_walk 주 5회이상 운동을 한 여부입니다.
 * @property total_sleep 총 수면시간입니다.
 */
data class PredictRequestDTO(
        private val sex : Int,
        private val age : Int,
        private val HE_sbp : Float?,
        private val HE_dbp : Float?,
        private val HE_BMI : Float?,
        private val HE_PLS : Float?,
        private val sm_present : Int,
        private val pa_walk : Int,
        private val total_sleep : Float?
)