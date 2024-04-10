package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.view.chart.type.HealthCategory

//수면시간 - 차트 매핑 클래스
class SleepHourChartMapper : ChartMapper<SleepHour>() {
    //수면시간을 시간단위로 변경해 Chart로 생성함.
    override fun convertToChart(entities: List<SleepHour>): Chart {
        return Chart(HealthCategory.SLEEP_HOUR, entities.map { ChartItem(it.date, it.duration.toHours().toDouble()) })
    }
}