package com.home.cdp2app.user.auth.sign.validator

import com.home.cdp2app.valid.type.ValidateStatus
import java.util.regex.Pattern

/**
 * 회원가입시 VM과 view를 validate하는 클래스
 */
class RegisterValidator {

    private val emailPattern: Pattern = Pattern.compile("^[^\\.\\s][\\w\\-\\.{2,}]+@([\\w-]+\\.)+[\\w-]{2,}\$")
    private val passwordPattern : Pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$")
    private val nicknamePattern : Pattern = Pattern.compile("[a-zA-Z가-힣0-9]{2,}")

    /**
     * 실제 검증을 수행하는 메소드
     * @param email 검증할 이메일 입니다. 이메일 형식이여야 합니다.
     * @param password 검증할 비밀번호입니다. 8-20자이며 특수문자 1개이상을 포함해야 합니다.
     * @param nickname 닉네임입니다. 한영숫자 2자 이상이여야 합니다.
     */
    fun validate(email : String?, password : String?, nickname : String?) : ValidateStatus {
        //email validate
        if (email.isNullOrBlank())
            return ValidateStatus.FIELD_EMPTY
        else if (!emailPattern.matcher(email).matches())
            return ValidateStatus.VALUE_ERROR //잘못된 이메일 형식


        //password valid
        if (password.isNullOrBlank())
            return ValidateStatus.FIELD_EMPTY
        else if (!passwordPattern.matcher(password).matches())
            return ValidateStatus.VALUE_ERROR


        if (nickname.isNullOrBlank())
            return ValidateStatus.FIELD_EMPTY
        else if (!nicknamePattern.matcher(nickname).matches())
            return ValidateStatus.VALUE_ERROR

        return ValidateStatus.OK
    }

}