package com.home.cdp2app.view.dialog.validator.sleep

import com.home.cdp2app.databinding.DialogSleephourBinding
import com.home.cdp2app.health.sleep.valid.SleepHourValidator
import com.home.cdp2app.type.ValidateStatus
import com.home.cdp2app.view.dialog.validator.ViewBindingDialogValidator

class SleepHourViewValidator(private val sleepHourValidator: SleepHourValidator) : ViewBindingDialogValidator<DialogSleephourBinding> {
    override fun validate(bind: DialogSleephourBinding): ValidateStatus {
        // date validate
        return sleepHourValidator.validate(bind.date.text.toString(), bind.sleepHour.text.toString())
    }
}