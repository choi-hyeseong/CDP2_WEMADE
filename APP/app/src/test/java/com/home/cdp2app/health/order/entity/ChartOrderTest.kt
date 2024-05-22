package com.home.cdp2app.health.order.entity

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.setting.order.entity.ChartOrder
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ChartOrderTest {

    //chart order map to chart test
    @Test
    fun TEST_MAP_TO_EMPTY_CHART() {
        val chartOrder = ChartOrder(linkedSetOf(HealthCategory.SLEEP_HOUR, HealthCategory.HEART_RATE, HealthCategory.BLOOD_PRESSURE_DIASTOLIC))
        val chart = chartOrder.toEmptyChart()

        //차트 확인
        assertEquals(3, chart.size)
        assertEquals(HealthCategory.SLEEP_HOUR, chart[0].type)
        assertTrue(chart[0].chartData.isEmpty())
        assertEquals(HealthCategory.HEART_RATE, chart[1].type)
        assertTrue(chart[1].chartData.isEmpty())
        assertEquals(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, chart[2].type)
        assertTrue(chart[2].chartData.isEmpty())
    }
}