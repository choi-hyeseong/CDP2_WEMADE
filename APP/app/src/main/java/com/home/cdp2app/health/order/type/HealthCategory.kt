package com.home.cdp2app.health.order.type

/**
 * 각 건강 차트별 사용할 카테고리 enum입니다.
 */
enum class HealthCategory(val displayName: String) {
    HEART_RATE("심박수"), BLOOD_PRESSURE_SYSTOLIC("수축기 혈압"), BLOOD_PRESSURE_DIASTOLIC("이완기 혈압"), SLEEP_HOUR("수면시간")
}