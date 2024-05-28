package com.home.cdp2app.health.heart.valid

import com.home.cdp2app.common.valid.type.ValidateStatus
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class HeartRateValidatorTest {

    private val validator : HeartRateValidator = HeartRateValidator()

    @Test
    fun TEST_VALID_OK() {
        assertEquals(ValidateStatus.OK, validator.validate("2024-01-01 15:00", "120"))
    }

    @Test
    fun TEST_DATE_EMPTY() {
        //date가 null인경우
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate(null, "120.0"))
        //date가 비어있는경우
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("", "120.0"))
    }

    @Test
    fun TEST_DATE_MALFORMED() {
        //date가 형식에 맞지않아 파싱되지 않은경우
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("15:00", "120"))
    }

    @Test
    fun TEST_HEART_RATE_EMPTY() {
        //심박수가 null
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("2024-01-01 15:00", null))
    }


    @Test
    fun TEST_HEART_RATE_LESS_THAN_REQUIRE() {
        //0이하인경우 오류
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("2024-01-01 15:00", "0"))
    }
}