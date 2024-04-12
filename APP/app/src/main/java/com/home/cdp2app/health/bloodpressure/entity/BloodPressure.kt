package com.home.cdp2app.health.bloodpressure.entity

import java.time.Instant

/**
 * 혈압 엔티티
 * @property date 측정한 날짜 입니다
 * @property systolic 수축기 혈압입니다.
 * @property diastolic 이완기 혈압입니다.
 */
data class BloodPressure(val date: Instant, val systolic: Double, val diastolic: Double)