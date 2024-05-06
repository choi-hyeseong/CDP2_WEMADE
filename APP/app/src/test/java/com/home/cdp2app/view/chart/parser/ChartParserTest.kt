package com.home.cdp2app.view.chart.parser

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.view.chart.mapper.BloodPressureSystolicChartMapperTest
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
            parser.parse(listOf(HeartRate(Instant.now(), 150L)), HealthCategory.HEART_RATE)
        }
    }

    @Test
    fun TEST_MAPPER_NOT_MATCHED() {
        //Parser의 mapper가 matching 되는게 없으므로 IllegalStateException 발생
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper()))
        assertThrows(IllegalStateException::class.java) {
            parser.parse(listOf(HeartRate(Instant.now(), 150L)), HealthCategory.HEART_RATE)
        }
    }

    @Test
    fun TEST_DATA_EMPTY() {
        //input data가 empty인경우 IllegalArgumentException 발생
        val parser = ChartParser(listOf(HeartRateChartMapper())) //HeartRate를 지원함
        assertThrows(IllegalArgumentException::class.java) {
            parser.parse(listOf<HeartRate>(), HealthCategory.HEART_RATE)
        }
    }

    @Test
    fun TEST_FAIL_NOT_CONVERTED_ENUM() {
        //해당 엔티티의 변환은 지원하지만, 요청하는 카테고리의 변환은 지원하지 않음 (BloodPressure -> BloodPressureSystolic / Diastolic..)
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper())) //diastolic을 지원함
        assertThrows(IllegalStateException::class.java) {
            parser.parse(listOf(BloodPressure(Instant.now(), 160.0, 120.0)), HealthCategory.BLOOD_PRESSURE_SYSTOLIC) //systolic을 요청함
        }
    }

    @Test
    fun TEST_FAIL_NOT_CONVERTED_ENUM_HEART_RATE() {
        //위 테스트와 동일하게 심박수의 경우에도 enum을 잘못주면 작동하지 않음
        val parser = ChartParser(listOf(HeartRateChartMapper())) //heartrate를 지원함
        assertThrows(IllegalStateException::class.java) {
            parser.parse(listOf(HeartRate(Instant.now(), 140)), HealthCategory.SLEEP_HOUR) //sleep hour로 요청함
        }
    }


    /*
    * Success part
    */

    @Test
    fun TEST_MAPPER_SUCCESS() {
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper(), SleepHourChartMapper(), HeartRateChartMapper())) //HeartRate를 지원함
        val chart = parser.parse(listOf(HeartRate(Instant.now(), 150L)), HealthCategory.HEART_RATE)
        assertEquals(HealthCategory.HEART_RATE, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(150.0, chart.chartData.first().data, 0.0)

    }

    @Test
    fun TEST_SUCCESS_CONVERTED_ENUM() {
        //enum까지 맞는경우 변환
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper())) //diastolic을 지원함
        assertDoesNotThrow {
            parser.parse(listOf(BloodPressure(Instant.now(), 160.0, 120.0)), HealthCategory.BLOOD_PRESSURE_DIASTOLIC) //diastolic으로 변환함
        }
    }

    @Test
    fun TEST_SUCCESS_CONVERTED_ENUM_DUPLICATE() {
        //enum까지 맞는경우 변환
        val parser = ChartParser(listOf(BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper())) //diastolic, systolic을 지원함
        assertDoesNotThrow {
            parser.parse(listOf(BloodPressure(Instant.now(), 160.0, 120.0)), HealthCategory.BLOOD_PRESSURE_SYSTOLIC) //systolic으로 변환함
        }
    }


    @Test
    fun TEST_MAPPER_SUCCESS_ON_DUPLICATE() {
        val parser = ChartParser(listOf(HeartRateChartMapper(), HeartRateChartMapper(),  HeartRateChartMapper(),  HeartRateChartMapper())) //HeartRate를 지원함
        val chart = parser.parse(listOf(HeartRate(Instant.now(), 150L)), HealthCategory.HEART_RATE)
        assertEquals(HealthCategory.HEART_RATE, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(150.0, chart.chartData.first().data, 0.0)
    }


}