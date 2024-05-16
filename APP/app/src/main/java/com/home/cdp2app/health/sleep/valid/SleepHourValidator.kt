package com.home.cdp2app.health.sleep.valid

import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.valid.type.ValidateStatus

/**
 * SleepHour validate class
 */
class SleepHourValidator {

    /**
     * validate 수행
     * @param date nullable한 날짜 문자열 (DateTimeUtil)
     * @param sleepHour nullable한 수면시간 (double형 만족, 0.1이하 금지)
     */
    fun validate(date : String?, sleepHour : String?) : ValidateStatus {
        if (date.isNullOrBlank()) return ValidateStatus.FIELD_EMPTY
        else if (DateTimeUtil.convertToDate(date.toString()) == null) return ValidateStatus.VALUE_ERROR

        // 비어있는경우 empty
        if (sleepHour.isNullOrBlank()) return ValidateStatus.FIELD_EMPTY

        // 성공여부 catch
        val success = kotlin.runCatching {
            val parsedSleepHour = sleepHour.toDouble()
            if (parsedSleepHour <= 0.1) throw IllegalArgumentException("0.1보다 낮아선 안됩니다.")
        }.isSuccess
        // 성공시 ok
        return if (success) ValidateStatus.OK
        //아닐경우 오류
        else ValidateStatus.VALUE_ERROR
    }
}