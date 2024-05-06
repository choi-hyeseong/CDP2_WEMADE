package com.home.cdp2app.health.order.entity

import com.home.cdp2app.health.order.type.HealthCategory

/**
 * RecyclerView에서 표시될 건강정보의 순서를 나타내는 클래스. List<HealthCategory>로 사용하려 했으나, List 제네릭을 class로 가져올 수 없음.
 * @property orders 순서정보 입니다.
 */
data class ChartOrder(private val orders: List<HealthCategory>)