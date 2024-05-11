package com.home.cdp2app.view.chart.formatter

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class DateFormatterTest {

    private val format =  SimpleDateFormat("MM-dd HH:mm", Locale.KOREA) //포맷 달라지면 오류 생길 수 있음

    @Test
    fun TEST_FORMAT_SUCCESS() {
        val instants = listOf(Instant.now(), Instant.now().minusSeconds(3600)) //현재시간과 1시간전 시간
        val dateFormatter = DateFormatter(instants)
        assertEquals(format.format(Date.from(instants[0])), dateFormatter.getFormattedValue(0.0f))
        assertEquals(format.format(Date.from(instants[0])), dateFormatter.getFormattedValue(0.5f)) //0번째 인덱스로 가져옴
        assertEquals(format.format(Date.from(instants[1])), dateFormatter.getFormattedValue(1.0f))
    }

    @Test
    fun TEST_OUT_OF_INDEX() {
        val instants = listOf(Instant.now(), Instant.now().minusSeconds(3600)) //현재시간과 1시간전 시간
        val dateFormatter = DateFormatter(instants)
        assertEquals("NULL", dateFormatter.getFormattedValue(3.0f)) //3번째 인덱스가 없음 - NULL 문자열 리턴
    }
}