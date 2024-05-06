package com.home.cdp2app.view.chart.parser.mapper

import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.health.order.type.HealthCategory
import kotlin.reflect.KClass

//수면시간 - 차트 매핑 클래스
class SleepHourChartMapper : ChartMapper<SleepHour>() {
    //수면시간을 시간단위로 변경해 Chart로 생성함.
    //TODO 시간단위를 좀더 계산해 1.5시간, 0.5시간으로 변경하기 (ex) 90분 수면 -> 1.5시간)
    override fun convertToChart(entities: List<SleepHour>): Chart {
        return Chart(HealthCategory.SLEEP_HOUR, entities.map { ChartItem(it.date, it.duration.toHours().toDouble()) })
    }

    // SleepHour data class를 지원하는 경우 true
    override fun isSupports(targetClass: KClass<*>): Boolean {
        return SleepHour::class == targetClass
    }
}