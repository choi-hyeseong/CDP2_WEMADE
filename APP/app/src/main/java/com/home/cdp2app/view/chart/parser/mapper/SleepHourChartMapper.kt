package com.home.cdp2app.view.chart.parser.mapper

import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.ChartItem
import com.home.cdp2app.health.order.type.HealthCategory
import java.text.DecimalFormat
import kotlin.reflect.KClass

//수면시간 - 차트 매핑 클래스
class SleepHourChartMapper : ChartMapper<SleepHour>() {
    //수면시간을 시간단위로 변경해 Chart로 생성함.
    private val decimalFormat = DecimalFormat("#.#")
    override fun convertToChart(entities: List<SleepHour>): Chart {
        return Chart(HealthCategory.SLEEP_HOUR, entities.map { ChartItem(it.date, decimalFormat.format(it.duration.toMinutes() / 60.0).toDouble()) })
    }

    // SleepHour data class를 지원하는 경우 true
    override fun isSupports(targetClass: KClass<*>): Boolean {
        return SleepHour::class == targetClass
    }

    // 수면시간으로 변환됨
    override fun isConvertTo(): HealthCategory {
        return HealthCategory.SLEEP_HOUR
    }
}