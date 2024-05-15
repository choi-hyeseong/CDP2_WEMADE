package com.home.cdp2app.view.fragment.auth.validator

import com.home.cdp2app.databinding.AuthLoginBinding
import com.home.cdp2app.type.ValidateStatus
import com.home.cdp2app.user.auth.validator.LoginValidator
import com.home.cdp2app.view.validator.ViewBindingValidator

class LoginViewValidator(private val loginValidator: LoginValidator) : ViewBindingValidator<AuthLoginBinding> {
    override fun validate(bind: AuthLoginBinding): ValidateStatus {
        return loginValidator.validate(bind.email.text.toString(), bind.password.text.toString())
    }

}