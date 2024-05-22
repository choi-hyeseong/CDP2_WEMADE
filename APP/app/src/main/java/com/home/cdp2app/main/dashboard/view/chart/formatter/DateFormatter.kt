package com.home.cdp2app.main.dashboard.view.chart.formatter

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

//instant를 float으로 변경하는게 아닌, Date를 따로 만들어서 index와 비교해서 보여주는 방식으로
class DateFormatter(val date: List<Instant>) : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        // null safety
        return kotlin.runCatching {
            SimpleDateFormat("MM-dd HH:mm", Locale.KOREA).format(Date.from(date[value.toInt()]))
        }.getOrElse { "NULL" }

    }
}