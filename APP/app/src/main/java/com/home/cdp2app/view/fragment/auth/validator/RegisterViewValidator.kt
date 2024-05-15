package com.home.cdp2app.view.fragment.auth.validator

import com.home.cdp2app.databinding.AuthRegisterBinding
import com.home.cdp2app.type.ValidateStatus
import com.home.cdp2app.user.auth.validator.RegisterValidator
import com.home.cdp2app.view.validator.ViewBindingValidator

class RegisterViewValidator(private val registerValidator: RegisterValidator) : ViewBindingValidator<AuthRegisterBinding> {
    override fun validate(bind: AuthRegisterBinding): ValidateStatus {
        return registerValidator.validate(bind.email.text.toString(), bind.password.text.toString(), bind.nickname.text.toString())
    }

}