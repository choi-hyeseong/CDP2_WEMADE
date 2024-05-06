package com.home.cdp2app.view.chart.parser

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.health.order.type.HealthCategory
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalArgumentException
import java.time.Instant

class ChartParserTest {

    /*
     * Exception Part
     */

    @Test
    fun TEST_MAPPER_EMPTY() {
        //Parser의 mapper가 비어있을경우 matching 되는게 없으므로 IllegalStateException 발생
        val parser = ChartParser(listOf())
        assertThrows(IllegalStateException::class.java) {
            parser.parse(listOf(HeartRate(Instant.now(), 150L)))
        }
    }

    @Test
    fun TEST_MAPPER_NOT_MATCHED() {
        //Parser의 mapper가 matching 되는게 없으므로 IllegalStateException 발생
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper()))
        assertThrows(IllegalStateException::class.java) {
            parser.parse(listOf(HeartRate(Instant.now(), 150L)))
        }
    }

    @Test
    fun TEST_DATA_EMPTY() {
        //input data가 empty인경우 IllegalArgumentException 발생
        val parser = ChartParser(listOf(HeartRateChartMapper())) //HeartRate를 지원함
        assertThrows(IllegalArgumentException::class.java) {
            parser.parse(listOf<HeartRate>())
        }
    }

    /*
    * Success part
    */

    @Test
    fun TEST_MAPPER_SUCCESS() {
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper(), SleepHourChartMapper(), HeartRateChartMapper())) //HeartRate를 지원함
        val chart = parser.parse(listOf(HeartRate(Instant.now(), 150L)))
        assertEquals(HealthCategory.HEART_RATE, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(150.0, chart.chartData.first().data, 0.0)

    }

    @Test
    fun TEST_MAPPER_SUCCESS_ON_DUPLICATE() {
        val parser = ChartParser(listOf(HeartRateChartMapper(), HeartRateChartMapper(),  HeartRateChartMapper(),  HeartRateChartMapper())) //HeartRate를 지원함
        val chart = parser.parse(listOf(HeartRate(Instant.now(), 150L)))
        assertEquals(HealthCategory.HEART_RATE, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(150.0, chart.chartData.first().data, 0.0)
    }


}