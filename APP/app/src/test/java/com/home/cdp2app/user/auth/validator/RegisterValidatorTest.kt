package com.home.cdp2app.user.auth.validator

import com.home.cdp2app.user.sign.validator.RegisterValidator
import com.home.cdp2app.common.valid.type.ValidateStatus
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class RegisterValidatorTest {

    private val validator = RegisterValidator()

    @Test
    fun TEST_EMAIL_EMPTY() {
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate(null, null, null))
    }

    @Test
    fun TEST_EMAIL_INVALID() {
        //email 형식에 맞지 않는경우
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf@.co", null, null))
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf @c.okr", null, null))
    }

    @Test
    fun TEST_EMAIL_VALID() {
        //email제외 나머지 valid
        assertEquals(ValidateStatus.OK, validator.validate("asdf@naver.com", "7564qwer!", "ttt"))
        assertEquals(ValidateStatus.OK, validator.validate("asdf@nav.co", "7564qwer!", "ttt"))
        assertEquals(ValidateStatus.OK, validator.validate("abc@knu.ac.kr", "7564qwer!", "ttt"))
    }

    @Test
    fun TEST_PASSWORD_EMPTY() {
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("asdf@nav.com", null, null))
    }

    @Test
    fun TEST_PASSWORD_INVALID() {
        //password 형식에 맞지 않는경우
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf@naver.com", "empty", null))
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf@naver.com", "emptypassword", null)) //영어만 있는경우
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf@naver.com", "emptypassword!", null)) //숫자가 없는경우
    }

    @Test
    fun TEST_PASSWORD_VALID() {
        assertEquals(ValidateStatus.OK, validator.validate("asdf@naver.com", "7564qwer!", "ttt"))
    }

    @Test
    fun TEST_NICKNAME_EMPTY() {
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("asdf@naver.com", "emptypass1!", null))
    }

    @Test
    fun TEST_NICKNAME_INVALID() {
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf@naver.com", "emptypass1!", "한"))
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("asdf@naver.com", "emptypass1!", "e"))
    }

    @Test
    fun TEST_NICKNAME_VALID() {
        assertEquals(ValidateStatus.OK, validator.validate("asdf@naver.com", "emptypass1!", "한영tw"))
    }
}