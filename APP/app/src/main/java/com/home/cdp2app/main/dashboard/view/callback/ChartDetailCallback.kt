package com.home.cdp2app.main.dashboard.view.callback

import com.home.cdp2app.main.setting.order.type.HealthCategory

/**
 * Fragment - Activity 전환에 필요한 콜백
 */
interface ChartDetailCallback {

    /**
     * 대시보드에서 상세정보로 이동할때 사용합니다.
     * @param category 어떤 차트의 상세 정보를 확인할지 정합니다.
     */
    fun navigateDetail(category: HealthCategory)

    /**
     * 설정에서 로그아웃시 초기 화면으로 이동할때 사용합니다. 이때 MainActivity로 이동합니다.
     */
    fun navigateMain()
}