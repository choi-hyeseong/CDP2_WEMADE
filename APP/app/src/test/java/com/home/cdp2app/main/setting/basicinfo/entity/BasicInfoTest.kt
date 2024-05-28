package com.home.cdp2app.main.setting.basicinfo.entity

import com.home.cdp2app.main.setting.basicinfo.type.Gender
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class BasicInfoTest {

    @Test
    fun TEST_CALC_BMI() {
        //180cm 75kg의 bmi는 23.1
        val weight = 75.0
        val height = 180.0
        val info = BasicInfo(height, weight, 23, Gender.WOMAN, false)

        assertEquals(23.1, info.calculateBMI())
    }
}