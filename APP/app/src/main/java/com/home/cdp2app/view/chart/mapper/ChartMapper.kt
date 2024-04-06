package com.home.cdp2app.view.chart.mapper

import com.home.cdp2app.view.chart.ChartItem

/**
 * Entity를 Chart의 List<ChartItem>으로 매핑시키는 클래스
 */
abstract class ChartMapper<T> {

    /**
     * 1:1로 매핑된 엔티티인 T를 chart로 변환시키는 함수
     */
    abstract fun convertToChart(entities : List<T>) : List<ChartItem>
}