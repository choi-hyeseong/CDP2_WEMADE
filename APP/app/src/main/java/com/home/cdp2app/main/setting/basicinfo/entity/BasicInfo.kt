package com.home.cdp2app.main.setting.basicinfo.entity

import com.home.cdp2app.main.setting.basicinfo.type.Gender

/**
 * 키, 몸무게, 흡연여부 등 기본 건강 정보를 담고 있는 엔티티
 * @property height 사용자의 키입니다.
 * @property weight 사용자의 체중입니다.
 * @property age 사용자의 나이입니다.
 * @property gender 사용자의 성별입니다.
 * @property isSmoking 사용자의 흡연 여부입니다.
 */
data class BasicInfo(val height: Double, val weight: Double, val age : Int, val gender : Gender, val isSmoking: Boolean) {

    /**
     * BMI를 계산하는 함수입니다.
     * @return 몸무게 / 키^2 한 결과가 반환됩니다.
     */
    fun calculateBMI() : Float {
        val heightMeter = (height / 100) //미터 단위로 변환한 후 제곱해야 결과 나옴
        return (weight / (heightMeter * heightMeter)).toFloat()
    }
}