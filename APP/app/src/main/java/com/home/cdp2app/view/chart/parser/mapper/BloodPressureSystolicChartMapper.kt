package com.home.cdp2app.view.chart.parser.mapper

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.view.chart.type.HealthCategory
import kotlin.reflect.KClass

/**
 * 혈압 엔티티를 수축기 혈압 차트로 매핑시키는 매퍼
 */
class BloodPressureSystolicChartMapper : ChartMapper<BloodPressure>() {
    override fun convertToChart(entities: List<BloodPressure>): Chart {
        return Chart(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, entities.map { ChartItem(it.date, it.systolic) })
    }

    // BloodPressure를 지원하는 경우 true
    override fun isSupports(targetClass: KClass<*>): Boolean {
        return BloodPressure::class == targetClass
    }
}