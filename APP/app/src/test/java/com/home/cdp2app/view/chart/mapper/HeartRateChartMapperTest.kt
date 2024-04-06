package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.view.chart.ChartItem
import org.junit.Assert.assertEquals
import org.junit.Test
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
        val items : List<ChartItem> = mapper.convertToChart(heartRates)
        assertEquals(2, items.size)
        //chart data는 double로 형변환 됨
        assertEquals(firstRate.time, items[0].time)
        assertEquals(firstRate.bpm.toDouble(), items[0].data, 0.0)
        assertEquals(secondRate.time, items[1].time)
        assertEquals(secondRate.bpm.toDouble(), items[1].data, 0.0)
    }

    @DisplayName("리스트가 빈경우 빈 차트 아이템 반환")
    @Test
    fun TEST_EMPTY_MAP_TO_CHART() {
        val heartRates = mutableListOf<HeartRate>()
        val items : List<ChartItem> = assertDoesNotThrow { mapper.convertToChart(heartRates) }
        assertEquals(0, items.size)

    }
}