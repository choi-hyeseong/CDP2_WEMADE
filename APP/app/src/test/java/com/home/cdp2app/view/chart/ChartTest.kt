package com.home.cdp2app.view.chart

import com.home.cdp2app.view.chart.type.HealthCategory
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class ChartTest {

    @Test
    fun TEST_CONVERT_TO_ENTRY() {
        // chart 생성
        val category = HealthCategory.HEART_RATE
        val chartItem = mutableListOf(ChartItem(Instant.now(), 100.0), ChartItem(Instant.now(), 50.0))
        val chart = Chart(category, chartItem)
        val entries = chart.chartData.toEntry()
        //내부 값들 일치 확인
        assertEquals(HealthCategory.HEART_RATE, chart.type)
        assertEquals("심박수", chart.type.displayName)
        assertEquals(2, entries.size)
        assertEquals((chartItem[1].time.epochSecond).toFloat(), entries[1].x)
        assertEquals(chartItem[1].data.toFloat(), entries[1].y)
    }
}