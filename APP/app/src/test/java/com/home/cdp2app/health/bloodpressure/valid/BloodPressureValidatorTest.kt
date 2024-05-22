package com.home.cdp2app.health.bloodpressure.valid

import com.home.cdp2app.common.valid.type.ValidateStatus
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class BloodPressureValidatorTest {

    private val validator : BloodPressureValidator = BloodPressureValidator()

    @Test
    fun TEST_VALID_OK() {
        assertEquals(ValidateStatus.OK, validator.validate("2024-01-01 15:00", "120.0", "80.0"))
    }

    @Test
    fun TEST_DATE_EMPTY() {
        //date가 null인경우
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate(null, "120.0", "80.0"))
        //date가 비어있는경우
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("", "120.0", "80.0"))
    }

    @Test
    fun TEST_DATE_MALFORMED() {
        //date가 형식에 맞지않아 파싱되지 않은경우
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("15:00", "120.0", "80.0"))
    }

    @Test
    fun TEST_PRESSURE_EMPTY() {
        //systolic이 null
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("2024-01-01 15:00", null, "80"))
        //diastolic이 null
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("2024-01-01 15:00", "120.0", null))
        //둘다 null
        assertEquals(ValidateStatus.FIELD_EMPTY, validator.validate("2024-01-01 15:00", null, null))
    }

    @Test
    fun TEST_DIASTOLIC_HIGHER_THAN_SYSTOLIC() {
        //이완기가 수축기보다 큰경우 error
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("2024-01-01 15:00", "70.0", "120.0"))
    }

    @Test
    fun TEST_PRESSURE_LESS_THAN_REQUIRE() {
        //10이하인경우 오류
        // 이완기가 10 이하
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("2024-01-01 15:00", "20.0", "10.0"))
        // 둘다 10 이하
        assertEquals(ValidateStatus.VALUE_ERROR, validator.validate("2024-01-01 15:00", "10.0", "9.0"))
    }

}