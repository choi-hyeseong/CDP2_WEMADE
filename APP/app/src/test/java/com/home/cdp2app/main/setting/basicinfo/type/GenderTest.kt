package com.home.cdp2app.main.setting.basicinfo.type

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class GenderTest {

    @Test
    fun TEST_MAN() {
        assertEquals(1, Gender.MAN.toIntValue())
    }

    @Test
    fun TEST_WOMAN() {
        assertEquals(2, Gender.WOMAN.toIntValue())
    }
}