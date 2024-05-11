package com.home.cdp2app.util.date

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Date

class DateTimeUtilTest {

    @Test
    fun TEST_CONVERT_TO_DATE_NULL_STRING() {
        // null string convert
        assertNull(DateTimeUtil.convertToDate(null))
    }

    @Test
    fun TEST_CONVERT_TO_DATE_SUCCESS() {
        // success convert
        val testInput = "2022-07-05 17:55"
        val requireOutput = LocalDateTime.parse(testInput, DateTimeUtil.dateFormatter).toInstant(ZonedDateTime.now().offset)
        assertEquals(requireOutput, DateTimeUtil.convertToDate(testInput))
    }

    @Test
    fun TEST_CONVERT_TO_DATE_FAIL_MALFORMED() {
        // malformed convert
        assertNull(DateTimeUtil.convertToDate("18:45"))
    }

    @Test
    fun TEST_CONVERT_TO_STRING_NULL_DATE() {
        // null instant to string
        assertNull(DateTimeUtil.convertToString(null))
    }

    @Test
    fun TEST_CONVERT_TO_STRING_SUCCESS() {
        val date = ZonedDateTime.now() //현재시간
        val instant = LocalDateTime.now().toInstant(ZonedDateTime.now().offset) //현재시간 Instant로 변환
        assertEquals(date.format(DateTimeUtil.dateFormatter), DateTimeUtil.convertToString(instant)) //직접 포맷한것과 util 사용한거 비교
    }
}