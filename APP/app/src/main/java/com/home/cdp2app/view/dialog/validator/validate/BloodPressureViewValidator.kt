package com.home.cdp2app.view.dialog.validator.validate

import com.home.cdp2app.databinding.DialogBloodpressureBinding
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus

//혈압 뷰 validate class
class BloodPressureViewValidator : ViewBindingDialogValidator<DialogBloodpressureBinding> {
    override fun validate(bind: DialogBloodpressureBinding): ValidateStatus {
        // date validate
        val dateString = bind.date.text
        if (dateString.isBlank()) return ValidateStatus.FIELD_EMPTY
        else if (DateTimeUtil.convertToDate(dateString.toString()) == null) return ValidateStatus.VALUE_ERROR

        // heart rate validate
        val systolicString = bind.bloodSystolic.text.toString()
        val diastolicString = bind.bloodDiastolic.text.toString()
        // 비어있는경우 empty
        if (systolicString.isBlank() || diastolicString.isBlank()) return ValidateStatus.FIELD_EMPTY

        // 성공여부 catch
        val success = kotlin.runCatching {
            val systolic = systolicString.toDouble()
            val diastolic = diastolicString.toDouble()
            if (systolic < diastolic) throw IllegalArgumentException("이완기가 수축기보다 높아선 안됩니다.")
            if (systolic <= 10 || diastolic <= 10) throw IllegalArgumentException("10보다 낮아선 안됩니다.")
        }.isSuccess
        // 성공시 ok
        return if (success) ValidateStatus.OK
        //아닐경우 오류
        else ValidateStatus.VALUE_ERROR
    }
}