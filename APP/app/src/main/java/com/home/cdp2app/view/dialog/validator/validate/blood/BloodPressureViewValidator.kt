package com.home.cdp2app.view.dialog.validator.validate.blood

import com.home.cdp2app.databinding.DialogBloodpressureBinding
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus
import com.home.cdp2app.view.dialog.validator.validate.ViewBindingDialogValidator

//혈압 뷰 validate class
class BloodPressureViewValidator(private val bloodPressureValidator: BloodPressureValidator) : ViewBindingDialogValidator<DialogBloodpressureBinding> {
    override fun validate(bind: DialogBloodpressureBinding): ValidateStatus {
        return bloodPressureValidator.validate(bind.date.text.toString(), bind.bloodSystolic.text.toString(), bind.bloodDiastolic.text.toString())
    }
}