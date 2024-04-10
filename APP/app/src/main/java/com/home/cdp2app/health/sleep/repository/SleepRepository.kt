package com.home.cdp2app.health.sleep.repository

import com.home.cdp2app.health.sleep.entity.SleepHour
import java.time.Instant

interface SleepRepository {

    /**
     * 해당 날짜 이전의 수면시간을 가져옵니다.
     * @param date 기준 날짜입니다.
     * @return SleepHour 엔티티 리스트를 반환합니다.
     */
    suspend fun readSleepHoursBefore(date : Instant) : List<SleepHour>

    /**
     * 수면시간을 기록합니다.
     * @param data SleepHour 엔티티입니다.
     */
    suspend fun writeSleepHours(data : List<SleepHour>)
}