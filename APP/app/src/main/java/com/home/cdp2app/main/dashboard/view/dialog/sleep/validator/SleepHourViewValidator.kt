package com.home.cdp2app.main.dashboard.view.dialog.sleep.validator

import com.home.cdp2app.common.valid.ViewBindingValidator
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.home.cdp2app.databinding.DialogSleephourBinding
import com.home.cdp2app.health.sleep.valid.SleepHourValidator

class SleepHourViewValidator(private val sleepHourValidator: SleepHourValidator) : ViewBindingValidator<DialogSleephourBinding> {
    override fun validate(bind: DialogSleephourBinding): ValidateStatus {
        // date validate
        return sleepHourValidator.validate(bind.date.text.toString(), bind.sleepHour.text.toString())
    }
}