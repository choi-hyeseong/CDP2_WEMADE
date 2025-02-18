package com.home.cdp2app.main.setting.order.repository

import com.home.cdp2app.common.memory.LocalDataStorage
import com.home.cdp2app.main.setting.order.entity.ChartOrder
import com.home.cdp2app.main.setting.order.type.HealthCategory

/**
 * SharedPreferences로 구현된 ChartOrder 레포지토리 입니다.
 * @property storage preference 접근에 사용되는 storage 입니다
 */
class PreferenceOrderRepository(private val storage: LocalDataStorage) : ChartOrderRepository {

    private val key : String = "PREFERENCE_ORDER"

    // ChartOrder 역직렬화
    override suspend fun loadOrder(): ChartOrder {
        return kotlin.runCatching {
            storage.loadObject(key, ChartOrder::class)
        }.getOrElse { ChartOrderRepository.DEFAULT }
    }

    // ChartOrder 저장
    override suspend fun saveOrder(order: ChartOrder) {
        //카테고리 전부 포함하는지 확인
        if(!order.orders.containsAll(HealthCategory.values().toList()))
            throw IllegalArgumentException("해당 순서쌍내에 일부 카테고리가 포함되어 있지 않습니다.")
        storage.saveObject(key, order)
    }

}