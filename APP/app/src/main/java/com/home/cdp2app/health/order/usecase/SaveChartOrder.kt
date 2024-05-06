package com.home.cdp2app.health.order.usecase

import com.home.cdp2app.health.order.entity.ChartOrder
import com.home.cdp2app.health.order.repository.ChartOrderRepository

/**
 * Chart 순서를 저장하는 유스케이스
 * @param chartOrderRepository 저장하는데 사용되는 레포지토리 입니다.
 */
class SaveChartOrder(private val chartOrderRepository: ChartOrderRepository) {

    /**
     * 순서를 저장하는 메소드
     * @param order 저장할 차트 순서입니다.
     * @see ChartOrderRepository.saveOrder()
     * @throws IllegalArgumentException order에 모든 HealthCategory enum 포함을 만족하지 않는경우 발생합니다
     */
    suspend operator fun invoke(order : ChartOrder) {
        chartOrderRepository.saveOrder(order)
    }
}