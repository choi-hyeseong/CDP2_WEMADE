package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.view.chart.type.HealthCategory

/**
 * 혈압 엔티티를 수축기 혈압 차트로 매핑시키는 매퍼
 */
class BloodPressureSystolicMapper : ChartMapper<BloodPressure>() {
    override fun convertToChart(entities: List<BloodPressure>): Chart {
        return Chart(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, entities.map { ChartItem(it.date, it.systolic) })
    }
}