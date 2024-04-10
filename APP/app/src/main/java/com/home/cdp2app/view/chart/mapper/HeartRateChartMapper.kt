package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.view.chart.type.HealthCategory

class HeartRateChartMapper : ChartMapper<HeartRate>() {

    override fun convertToChart(entities: List<HeartRate>): Chart {
        return Chart(HealthCategory.HEART_RATE, entities.map { ChartItem(it.time, it.bpm.toDouble()) })
    }
}