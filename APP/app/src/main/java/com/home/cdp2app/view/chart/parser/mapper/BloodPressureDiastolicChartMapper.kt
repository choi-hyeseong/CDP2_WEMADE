package com.home.cdp2app.view.chart.parser.mapper

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.health.order.type.HealthCategory
import kotlin.reflect.KClass

/**
 * 혈압 엔티티를 이완기 혈압 차트로 매핑시키는 매퍼
 */
class BloodPressureDiastolicChartMapper : ChartMapper<BloodPressure>() {
    override fun convertToChart(entities: List<BloodPressure>): Chart {
        return Chart(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, entities.map { ChartItem(it.date, it.diastolic) })
    }

    // BloodPressure를 지원하는 경우 true
    override fun isSupports(targetClass: KClass<*>): Boolean {
        return BloodPressure::class == targetClass
    }

    // diastolic으로 변환됨
    override fun isConvertTo(): HealthCategory {
        return HealthCategory.BLOOD_PRESSURE_DIASTOLIC
    }

}