package com.home.cdp2app.main.setting.order.type

/**
 * 각 건강 차트별 사용할 카테고리 enum입니다.
 */
enum class HealthCategory(val displayName: String) {

    HEART_RATE("심박수"), BLOOD_PRESSURE_SYSTOLIC("혈압(수축기)"), BLOOD_PRESSURE_DIASTOLIC("혈압(이완기)"), SLEEP_HOUR("수면시간");
    companion object {
        /**
         * 문자열로부터 Category를 파싱하는 메소드 입니다.
         * @param str nullable한 문자열입니다.
         * @return NPE든, 올바르지 않은 값이든 파싱 실패시 null을 반환하고, 파싱 성공시 Category를 반환합니다.
         */
        fun fromString(str : String?) : HealthCategory? {
            return kotlin.runCatching {
                HealthCategory.valueOf(str!!)
            }.getOrNull()
        }
    }
}