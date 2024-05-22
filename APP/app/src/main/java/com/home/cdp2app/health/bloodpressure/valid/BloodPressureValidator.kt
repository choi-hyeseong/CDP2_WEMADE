package com.home.cdp2app.health.bloodpressure.valid

import com.home.cdp2app.common.util.date.DateTimeUtil
import com.home.cdp2app.common.valid.type.ValidateStatus

/**
 * VM, ViewValidator에서 사용하는 validation class. VM과 View에서 validation을 수행할때 코드를 중복시키지 않기 위함. 어처피 value valid의 목적은 같으니
 */
class BloodPressureValidator {

    /**
     * validate 수행
     * @param date nullable한 날짜 문자열 입니다. format은 DateTimeUtil 참조
     * @param systolic nullable한 수축기 혈압 문자열 입니다. double형 만족해야함, 10초과
     * @param diastolic nullable한 이완기 혈압 문자열 입니다. double형 만족해야함 10초과
     */
    fun validate(date : String?, systolic : String?, diastolic : String?) : ValidateStatus {
        // date validate
        if (date.isNullOrBlank()) return ValidateStatus.FIELD_EMPTY
        else if (DateTimeUtil.convertToDate(date.toString()) == null) return ValidateStatus.VALUE_ERROR

        // 비어있는경우 empty
        if (systolic.isNullOrBlank() || diastolic.isNullOrBlank()) return ValidateStatus.FIELD_EMPTY

        // 성공여부 catch
        val success = kotlin.runCatching {
            val parsedSystolic = systolic.toDouble()
            val parsedDiastolic = diastolic.toDouble()
            if (parsedSystolic < parsedDiastolic) throw IllegalArgumentException("이완기가 수축기보다 높아선 안됩니다.")
            if (parsedSystolic <= 10 || parsedDiastolic <= 10) throw IllegalArgumentException("10보다 낮아선 안됩니다.")
        }.isSuccess
        // 성공시 ok
        return if (success) ValidateStatus.OK
        //아닐경우 오류
        else ValidateStatus.VALUE_ERROR
    }
}