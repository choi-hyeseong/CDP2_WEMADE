package com.home.cdp2app.health.order.repository

import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.health.order.entity.ChartOrder

/**
 * SharedPreferences로 구현된 ChartOrder 레포지토리 입니다.
 * @property preferencesStorage preference 접근에 사용되는 storage 입니다
 */
class PreferenceOrderRepository(private val preferencesStorage: SharedPreferencesStorage) : ChartOrderRepository {

    private val key : String = "PREFERENCE_ORDER"

    // ChartOrder 역직렬화
    override suspend fun loadOrder(): ChartOrder {
        return kotlin.runCatching {
            preferencesStorage.loadObject(key, ChartOrder::class)
        }.getOrElse { ChartOrderRepository.DEFAULT }
    }

    // ChartOrder 저장
    override suspend fun saveOrder(order: ChartOrder) {
        preferencesStorage.saveObject(key, order)
    }

}