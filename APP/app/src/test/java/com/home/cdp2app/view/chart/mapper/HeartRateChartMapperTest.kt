package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.main.dashboard.view.chart.Chart
import com.home.cdp2app.main.dashboard.view.chart.parser.mapper.HeartRateChartMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Instant

class HeartRateChartMapperTest {

    val mapper = HeartRateChartMapper()

    @DisplayName("차트 아이템으로 매핑 테스트")
    @Test
    fun TEST_MAP_TO_CHART() {
        val firstRate = HeartRate(Instant.now().minusSeconds(1), 150)
        val secondRate = HeartRate(Instant.now().minusSeconds(2), 140)
        val heartRates = mutableListOf(firstRate, secondRate)
        val chart: Chart = mapper.convertToChart(heartRates)
        assertEquals(2, chart.chartData.size)
        //chart data는 double로 형변환 됨
        assertEquals(firstRate.time, chart.chartData[0].time)
        assertEquals(firstRate.bpm.toDouble(), chart.chartData[0].data, 0.0)
        assertEquals(secondRate.time, chart.chartData[1].time)
        assertEquals(secondRate.bpm.toDouble(), chart.chartData[1].data, 0.0)
    }

    @DisplayName("리스트가 빈경우 빈 차트 아이템 반환")
    @Test
    fun TEST_EMPTY_MAP_TO_CHART() {
        val heartRates = mutableListOf<HeartRate>()
        val chart: Chart = assertDoesNotThrow { mapper.convertToChart(heartRates) }
        assertEquals(0, chart.chartData.size)

    }

    @Test
    fun TEST_IS_SUPPORTS() {
        Assertions.assertTrue(mapper.isSupports(HeartRate::class))
    }

    @Test
    fun TEST_IS_NOT_SUPPORTS() {
        Assertions.assertFalse(mapper.isSupports(SleepHour::class))
    }

    @Test
    fun TEST_IS_COVERTED_TO() {
        Assertions.assertEquals(HealthCategory.HEART_RATE, mapper.isConvertTo())
    }

    @Test
    fun TEST_IS_NOT_CONVERTED_TO() {
        Assertions.assertNotEquals(HealthCategory.SLEEP_HOUR, mapper.isConvertTo())
    }
}