package com.home.cdp2app.main.setting.order.repository

import com.home.cdp2app.main.setting.order.entity.ChartOrder
import com.home.cdp2app.main.setting.order.repository.ChartOrderRepository.Companion.DEFAULT
import com.home.cdp2app.main.setting.order.type.HealthCategory

/**
 * RecyclerView에서 차트의 표현 순서를 저장하는 클래스
 * @property DEFAULT 저장된 순서가 없을경우 기본으로 불러올 순서입니다.
 */
interface ChartOrderRepository {

    companion object {
        val DEFAULT : ChartOrder = ChartOrder(LinkedHashSet(HealthCategory.values().toList()))
    }

    /**
     * 순서를 불러오는 메소드 입니다.
     * @return 리턴된 ChartOrder 입니다. 저장된 값이 없을경우 ChartOrderRepository.DEFAULT를 반환합니다.
     */
    suspend fun loadOrder() : ChartOrder

    /**
     * 순서를 저장하는 메소드 입니다.
     * @param order 저장할 순서 값 입니다.
     * @throws IllegalArgumentException 해당 순서내에 HealthCategory enum이 전부 포함되어야 하나, 그렇지 않은경우 발생합니다. (미 포함시 차트에 안보임)
     */
    suspend fun saveOrder(order : ChartOrder)
}