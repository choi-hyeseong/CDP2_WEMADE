package com.home.cdp2app.main.dashboard.view.dialog.heart.validator

import com.home.cdp2app.common.valid.ViewBindingValidator
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.home.cdp2app.databinding.DialogHeartBinding
import com.home.cdp2app.health.heart.valid.HeartRateValidator

/**
 * DialogHeartBinding 뷰를 검증하는 클래스
 */
class HeartRateViewValidator(private val heartRateValidator: HeartRateValidator) : ViewBindingValidator<DialogHeartBinding> {
    override fun validate(bind: DialogHeartBinding): ValidateStatus {
        // date validate
        return heartRateValidator.validate(bind.date.text.toString(), bind.heartRate.text.toString())
    }

}