package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.view.chart.ChartItem

class HeartRateChartMapper : ChartMapper<HeartRate>() {

    override fun convertToChart(entities: List<HeartRate>): List<ChartItem> {
        return entities.map { ChartItem(it.time, it.bpm.toDouble()) }
    }
}