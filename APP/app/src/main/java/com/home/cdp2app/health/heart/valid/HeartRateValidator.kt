package com.home.cdp2app.health.heart.valid

import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.type.ValidateStatus

/**
 * 심박수 validating class
 */
class HeartRateValidator {

    /**
     * 심박수 validate
     * @param date nullable한 날짜. 포맷은 DateTimeUtil 참조
     * @param heartRate nullable한 심박수 문자열 (0 초과 필수)
     */
    fun validate(date : String?, heartRate : String?) : ValidateStatus {
        if (date.isNullOrBlank()) return ValidateStatus.FIELD_EMPTY
        else if (DateTimeUtil.convertToDate(date.toString()) == null) return ValidateStatus.VALUE_ERROR

        if (heartRate.isNullOrBlank()) return ValidateStatus.FIELD_EMPTY

        // 성공여부 catch
        val success = kotlin.runCatching {
            val parsedHeartRate = heartRate.toLong()
            if (parsedHeartRate <= 0) throw IllegalArgumentException("0보다 낮아선 안됩니다.")
        }.isSuccess
        // 성공시 ok
        return if (success) ValidateStatus.OK
        //아닐경우 오류
        else ValidateStatus.VALUE_ERROR
    }
}