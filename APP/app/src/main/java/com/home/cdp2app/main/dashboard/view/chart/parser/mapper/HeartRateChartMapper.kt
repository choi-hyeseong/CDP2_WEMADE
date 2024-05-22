package com.home.cdp2app.main.dashboard.view.chart.parser.mapper

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.main.dashboard.view.chart.Chart
import com.home.cdp2app.main.dashboard.view.chart.ChartItem
import com.home.cdp2app.main.setting.order.type.HealthCategory
import kotlin.reflect.KClass

class HeartRateChartMapper : ChartMapper<HeartRate>() {

    override fun convertToChart(entities: List<HeartRate>): Chart {
        return Chart(HealthCategory.HEART_RATE, entities.map { ChartItem(it.time, it.bpm.toDouble()) })
    }

    // HeartRate data class를 지원하는경우
    override fun isSupports(targetClass: KClass<*>): Boolean {
        return HeartRate::class == targetClass
    }

    // heart rate로 변환됨
    override fun isConvertTo(): HealthCategory {
        return HealthCategory.HEART_RATE
    }
}