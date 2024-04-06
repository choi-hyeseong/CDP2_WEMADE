package com.home.cdp2app.view.chart

import com.github.mikephil.charting.data.Entry
import com.home.cdp2app.view.chart.type.HealthCategory
import java.time.Instant

/**
 * RecyclerView에 표시되는 하나의 Chart 데이터 입니다.
 * @property type 해당 차트의 카테고리입니다.
 * @property chartData 하나의 Line을 아이템의 데이터입니다.
 */
data class Chart(
    val type : HealthCategory,
    val chartData : List<ChartItem>
)

/**
 * 한 라인의 점 하나하나를 표현하는 클래스 입니다. (ex) 10월 1일날 150bpm)
 * @property time 측정된 시간 입니다.
 * @property data 측정된 값입니다.
 */
data class ChartItem(
    val time : Instant,
    val data : Double
)

//LineChart에서 사용하는 Entry로 변환.
// TODO index of 제거하고 time으로 바꿔서 추후 일자별로 수정
fun List<ChartItem>.toEntry() : List<Entry> = this.map { Entry(this.indexOf(it).toFloat(), it.data.toFloat()) }