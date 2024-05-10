package com.home.cdp2app.view.chart

import com.home.cdp2app.health.order.type.HealthCategory

/**
 * Fragment - Activity 전환에 필요한 콜백
 */
interface ChartCallback {

    /**
     * 대시보드에서 상세정보로 이동할때 사용합니다.
     * @param category 어떤 차트의 상세 정보를 확인할지 정합니다.
     */
    fun navigateDetail(category: HealthCategory)
}