package com.home.cdp2app.view.dialog.validator.validate

import com.home.cdp2app.databinding.DialogSleephourBinding
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus

class SleepHourViewValidator : ViewBindingDialogValidator<DialogSleephourBinding> {
    override fun validate(bind: DialogSleephourBinding): ValidateStatus {
        // date validate
        val dateString = bind.date.text
        if (dateString.isBlank()) return ValidateStatus.FIELD_EMPTY
        else if (DateTimeUtil.convertToDate(dateString.toString()) == null) return ValidateStatus.VALUE_ERROR

        // heart rate validate
        val sleepHourString = bind.sleepHour.text.toString()
        // 비어있는경우 empty
        if (sleepHourString.isBlank()) return ValidateStatus.FIELD_EMPTY

        // 성공여부 catch
        val success = kotlin.runCatching {
            val sleepHour = sleepHourString.toDouble()
            if (sleepHour <= 0) throw IllegalArgumentException("0보다 낮아선 안됩니다.")
        }.isSuccess
        // 성공시 ok
        return if (success) ValidateStatus.OK
        //아닐경우 오류
        else ValidateStatus.VALUE_ERROR
    }
}