package com.home.cdp2app.health.order.type

import com.home.cdp2app.main.setting.order.type.HealthCategory
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class HealthCategoryTest {

    @Test
    fun TEST_PARSE_SUCCESS() {
        val result = HealthCategory.fromString("BLOOD_PRESSURE_SYSTOLIC") //혈압 systolic enum
        assertNotNull(result)
        assertEquals(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, result)
    }

    @Test
    fun TEST_PARSE_FAIL_PARAM_NULL() {
        val result = HealthCategory.fromString(null) //null param
        assertNull(result)
    }

    @Test
    fun TEST_INVALID_ENUM() {
        val result = HealthCategory.fromString("HEIGHT") //키 param? - 존재하지 않음
        assertNull(result)
    }
}