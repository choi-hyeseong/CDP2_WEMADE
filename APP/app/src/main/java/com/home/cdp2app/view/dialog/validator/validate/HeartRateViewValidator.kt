package com.home.cdp2app.view.dialog.validator.validate

import com.home.cdp2app.databinding.DialogHeartBinding
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus

/**
 * DialogHeartBinding 뷰를 검증하는 클래스
 */
class HeartRateViewValidator : ViewBindingDialogValidator<DialogHeartBinding> {
    override fun validate(bind: DialogHeartBinding): ValidateStatus {
        // date validate
        val dateString = bind.date.text
        if (dateString.isBlank()) return ValidateStatus.FIELD_EMPTY
        else if (DateTimeUtil.convertToDate(dateString.toString()) == null) return ValidateStatus.VALUE_ERROR

        // heart rate validate
        val heartRateString = bind.heartRate.text.toString()
        // 비어있는경우 empty
        if (heartRateString.isBlank()) return ValidateStatus.FIELD_EMPTY

        // 성공여부 catch
        val success = kotlin.runCatching {
            val heartRate = heartRateString.toLong()
            if (heartRate <= 0) throw IllegalArgumentException("0보다 낮아선 안됩니다.")
        }.isSuccess
        // 성공시 ok
        return if (success) ValidateStatus.OK
        //아닐경우 오류
        else ValidateStatus.VALUE_ERROR
    }

}