package com.home.cdp2app.main.dashboard.view.dialog.blood.validator

import com.home.cdp2app.common.valid.ViewBindingValidator
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.home.cdp2app.databinding.DialogBloodpressureBinding
import com.home.cdp2app.health.bloodpressure.valid.BloodPressureValidator

//혈압 뷰 validate class
class BloodPressureViewValidator(private val bloodPressureValidator: BloodPressureValidator) : ViewBindingValidator<DialogBloodpressureBinding> {
    override fun validate(bind: DialogBloodpressureBinding): ValidateStatus {
        return bloodPressureValidator.validate(bind.date.text.toString(), bind.bloodSystolic.text.toString(), bind.bloodDiastolic.text.toString())
    }
}