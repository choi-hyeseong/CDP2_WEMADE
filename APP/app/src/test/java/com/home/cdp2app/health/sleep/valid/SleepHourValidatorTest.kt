package com.home.cdp2app.health.sleep.valid

import com.home.cdp2app.common.valid.type.ValidateStatus
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SleepHourValidatorTest {
    private val validator : SleepHourValidator = SleepHourValidator()

    @Test
    fun TEST_VALID_OK() {
        assertEquals(ValidateStatus.OK, validator.validate("2024-01-01 15:00", "9.5"))
    }

    @Test
    fun TEST_DATE_EMPTY() {
        //date가 null인경우
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate(null, "9.0"))
        //date가 비어있는경우
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("", "9.0"))
    }

    @Test
    fun TEST_DATE_MALFORMED() {
        //date가 형식에 맞지않아 파싱되지 않은경우
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("15:00", "9"))
    }

    @Test
    fun TEST_SLEEP_HOUR_EMPTY() {
        //수면시간 null
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("2024-01-01 15:00", null))
    }


    @Test
    fun TEST_SLEEP_HOUR_LESS_THAN_REQUIRE() {
        //0.1이하인경우 오류
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("2024-01-01 15:00", "0.1"))
    }
}