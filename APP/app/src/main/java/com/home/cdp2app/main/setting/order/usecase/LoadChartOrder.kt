package com.home.cdp2app.main.setting.order.usecase

import com.home.cdp2app.main.setting.order.entity.ChartOrder
import com.home.cdp2app.main.setting.order.repository.ChartOrderRepository

/**
 * Chart의 순서를 가져오는 유스케이스
 */
class LoadChartOrder(private val orderRepository: ChartOrderRepository) {

    /**
     * 차트 순서 불러오는 메소드.
     * @see ChartOrderRepository.loadOrder()
     * @return 차트의 순서를 불러옵니다. 저장된 순서가 없을경우 ChartOrderRepository.DEFAULT 값을 반환합니다.
     */
    suspend operator fun invoke() : ChartOrder {
        return orderRepository.loadOrder()
    }
}