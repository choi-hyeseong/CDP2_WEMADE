package com.home.cdp2app.main.setting.order.entity

import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.dashboard.chart.Chart

/**
 * RecyclerView에서 표시될 건강정보의 순서를 나타내는 클래스. List<HealthCategory>로 사용하려 했으나, List 제네릭을 class로 가져올 수 없음.
 * 카테고리에는 반드시 모든 HealthCategory가 중복되지 않고 하나씩 들어가야 합니다. 생성시 LinkedHashSet(List())의 형태로 생성할 것을 추천 Set(Set)구조면 순서 틀어질 가능성 있음
 * @property orders 순서정보 입니다. 반드시 LinkedHashSet으로 지정되어야 합니다. (순서 보장)
 */
data class ChartOrder(val orders: LinkedHashSet<HealthCategory>) {

    fun toEmptyChart() : MutableList<Chart> = orders.map { Chart(it, mutableListOf()) }.toMutableList() //빈 차트로 매핑해주는 함수

    // 내부 값 갱신용
    fun update(categories : Collection<HealthCategory>) {
        orders.clear()
        orders.addAll(categories)
    }
}