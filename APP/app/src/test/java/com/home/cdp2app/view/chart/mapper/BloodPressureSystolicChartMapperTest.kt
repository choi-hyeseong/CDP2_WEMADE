package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.main.dashboard.view.chart.Chart
import com.home.cdp2app.main.dashboard.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.main.setting.order.type.HealthCategory
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.Instant

//수축기 혈압 매퍼 테스트
class BloodPressureSystolicChartMapperTest {

    private val bloodPressureSystolicMapper = BloodPressureSystolicChartMapper()

    @Test
    fun TEST_MAP_TO_CHART() {
        val firstDiastolic = BloodPressure(Instant.now().minusSeconds(1), 120.0, 70.0) //1초전에 측정한 120/70의 혈압 엔티티
        val secondDiastolic = BloodPressure(Instant.now(), 125.0, 75.0) //방금 측정한 125/75의 혈압 엔티티
        val mappedChart = bloodPressureSystolicMapper.convertToChart(listOf(firstDiastolic, secondDiastolic))
        //데이터 일치하는지 확인
        Assert.assertEquals(2, mappedChart.chartData.size)
        listOf(firstDiastolic, secondDiastolic).forEachIndexed { index, pressure ->
            assertEquals(pressure.date, mappedChart.chartData[index].time)
            assertEquals(pressure.systolic, mappedChart.chartData[index].data)
        }
        assertEquals(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, mappedChart.type)
    }


    @Test
    fun TEST_EMPTY_MAP_TO_CHART() {
        val data = listOf<BloodPressure>()
        val chart: Chart = org.junit.jupiter.api.assertDoesNotThrow { bloodPressureSystolicMapper.convertToChart(data) }
        Assert.assertEquals(0, chart.chartData.size)
        assertEquals(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, chart.type)

    }

    @Test
    fun TEST_IS_SUPPORTS() {
        Assertions.assertTrue(bloodPressureSystolicMapper.isSupports(BloodPressure::class))
    }

    @Test
    fun TEST_IS_NOT_SUPPORTS() {
        Assertions.assertFalse(bloodPressureSystolicMapper.isSupports(SleepHour::class))
    }

    @Test
    fun TEST_IS_COVERTED_TO() {
        assertEquals(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, bloodPressureSystolicMapper.isConvertTo())
    }

    // diastolic은 미지원
    @Test
    fun TEST_IS_NOT_CONVERTED_TO() {
        Assertions.assertNotEquals(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, bloodPressureSystolicMapper.isConvertTo())
    }
}