package com.home.cdp2app.common.util.primitive

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class BooleanUtilKtTest {

    @Test
    fun TEST_BOOLEAN_TO_INT() {
        assertEquals(1, true.toInt())
        assertEquals(0, false.toInt())
    }
}