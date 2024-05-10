package com.home.cdp2app.view.dialog.validator.validate.heart

import com.home.cdp2app.databinding.DialogHeartBinding
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus
import com.home.cdp2app.view.dialog.validator.validate.ViewBindingDialogValidator

/**
 * DialogHeartBinding 뷰를 검증하는 클래스
 */
class HeartRateViewValidator(private val heartRateValidator: HeartRateValidator) : ViewBindingDialogValidator<DialogHeartBinding> {
    override fun validate(bind: DialogHeartBinding): ValidateStatus {
        // date validate
        return heartRateValidator.validate(bind.date.text.toString(), bind.heartRate.text.toString())
    }

}