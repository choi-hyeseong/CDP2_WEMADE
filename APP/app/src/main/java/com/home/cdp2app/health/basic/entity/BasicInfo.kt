package com.home.cdp2app.health.basic.entity

import com.home.cdp2app.health.basic.type.Gender

/**
 * 키, 몸무게, 흡연여부 등 기본 건강 정보를 담고 있는 엔티티
 * @property height 사용자의 키입니다.
 * @property weight 사용자의 체중입니다.
 * @property gender 사용자의 성별입니다.
 * @property isSmoking 사용자의 흡연 여부입니다.
 */
data class BasicInfo(private val height: Double, private val weight: Double, private val gender : Gender ,private val isSmoking: Boolean)