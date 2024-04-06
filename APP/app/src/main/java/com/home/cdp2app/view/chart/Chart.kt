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

/**
 * Chart를 구성하는 Item을 Entry로 변환함. 이때 float - Int(Long)간 전환시 시간 부분에 있어 오차가 발생할 수 있다. (부동소수점 문제)
 * @return Entry(Float, Float)으로 반환하며, 이때 X값은 time.epochSecond를 float으로 변환한 값이다. Y값은 data를 float으로 변환한 값이다.
 */
fun List<ChartItem>.toEntry() : List<Entry> = this.map { Entry(it.time.epochSecond.toFloat(), it.data.toFloat()) }