package com.home.cdp2app.view.dialog.validator.validate.sleep

import com.home.cdp2app.databinding.DialogSleephourBinding
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus
import com.home.cdp2app.view.dialog.validator.validate.ViewBindingDialogValidator

class SleepHourViewValidator(private val sleepHourValidator: SleepHourValidator) : ViewBindingDialogValidator<DialogSleephourBinding> {
    override fun validate(bind: DialogSleephourBinding): ValidateStatus {
        // date validate
        return sleepHourValidator.validate(bind.date.text.toString(), bind.sleepHour.text.toString())
    }
}