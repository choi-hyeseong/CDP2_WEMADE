package com.home.cdp2app.view.chart

import com.home.cdp2app.health.order.type.HealthCategory
import java.time.Instant

/**
 * RecyclerView에 표시되는 하나의 Chart 데이터 입니다.
 * @property type 해당 차트의 카테고리입니다.
 * @property chartData 하나의 Line을 아이템의 데이터입니다.
 */
data class Chart(val type: HealthCategory, val chartData: List<ChartItem>)

/**
 * 한 라인의 점 하나하나를 표현하는 클래스 입니다. (ex) 10월 1일날 150bpm)
 * @property time 측정된 시간 입니다.
 * @property data 측정된 값입니다.
 */
data class ChartItem(val time: Instant, val data: Double)

/**
 * Chart MutableList에 해당 Chart를 apply하는 확장 함수
 *
 * ChartOrder.toEmptyChart로 반환된 Enum만 가진 빈 차트에 값을 채워넣을때 사용됩니다.
 * @param targetChart 값을 넣을 차트 엔티티입니다.
 * @throws IllegalStateException 빈 차트에 해당 enum이 없을경우 발생합니다.
 */
fun MutableList<Chart>.applyChart(targetChart : Chart) {
    val categoryIndex = this.indexOfFirst { it.type == targetChart.type } //해당 enum을 가진 chart 탐색
    if (categoryIndex == -1)
        //없는경우 - 순서 등록이 안되어있을때 (enum 미포함)
        throw IllegalStateException("해당 enum을 찾을 수 없습니다.")
    this[categoryIndex] = targetChart //차트 업데이트
}