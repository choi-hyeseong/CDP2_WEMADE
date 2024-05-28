package com.home.cdp2app.user.sign.view.login.validator

import com.home.cdp2app.common.valid.ViewBindingValidator
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.home.cdp2app.databinding.AuthLoginBinding
import com.home.cdp2app.user.sign.validator.LoginValidator

//로그인 뷰 validator
class LoginViewValidator(private val loginValidator: LoginValidator) : ViewBindingValidator<AuthLoginBinding> {
    override fun validate(bind: AuthLoginBinding): ValidateStatus {
        return loginValidator.validate(bind.email.text.toString(), bind.password.text.toString())
    }

}