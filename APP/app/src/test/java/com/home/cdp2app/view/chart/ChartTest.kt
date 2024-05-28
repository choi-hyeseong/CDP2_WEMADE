package com.home.cdp2app.view.chart

import com.home.cdp2app.main.dashboard.chart.Chart
import com.home.cdp2app.main.dashboard.chart.ChartItem
import com.home.cdp2app.main.dashboard.chart.applyChart
import com.home.cdp2app.main.setting.order.entity.ChartOrder
import com.home.cdp2app.main.setting.order.type.HealthCategory
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class ChartTest {

    @Test
    fun TEST_APPLY_CHART_SUCCESS() {
        val chart = ChartOrder(LinkedHashSet(listOf(HealthCategory.HEART_RATE, HealthCategory.SLEEP_HOUR))).toEmptyChart()
        val sleepHourChart = Chart(
            HealthCategory.SLEEP_HOUR, listOf(
                ChartItem(Instant.now(), 1.5), ChartItem(
            Instant.now(), 3.0)
        ))
        assertDoesNotThrow {
            chart.applyChart(sleepHourChart)
        }
        assertEquals(2, chart[1].chartData.size)
        assertEquals(1.5, chart[1].chartData[0].data, 0.0)
    }

    @Test
    fun TEST_APPLY_CHART_FAIL_NOT_FOUND_INDEX() {
        val chart = ChartOrder(LinkedHashSet(listOf(HealthCategory.HEART_RATE, HealthCategory.SLEEP_HOUR))).toEmptyChart()
        // systolic은 등록되지 않음
        val systolicChart = Chart(
            HealthCategory.BLOOD_PRESSURE_SYSTOLIC, listOf(
                ChartItem(Instant.now(), 1.5), ChartItem(
            Instant.now(), 3.0)
        ))
        assertThrows(IllegalStateException::class.java) {
            chart.applyChart(systolicChart)
        }
    }
}