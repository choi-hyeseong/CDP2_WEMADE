package com.home.cdp2app.health.sleep.entity

import java.time.Duration
import java.time.Instant

/**
 * 수면시간 엔티티
 * @property date 수면을 시작한 시간입니다
 * @property duration 수면시간입니다.
 */
data class SleepHour(val date: Instant, val duration: Duration)