package com.home.cdp2app.health.heart.repository

import com.home.cdp2app.health.heart.entity.HeartRate
import java.time.Instant

//심박수를 읽기 위한 repository
interface HeartRepository {

    /**
     * 특정 날짜 이전까지 기록된 모든 심박수를 가져오는 함수
     * @param date 기준 날짜 입니다.
     * @return HeartRate List입니다.
     */
    suspend fun readHeartRateBefore(date: Instant): List<HeartRate>

    /**
     * 일정 기간동안 기록되 심박수를 저장하는 함수
     * @param heartList 심박수 엔티티 list입니다.
     */
    suspend fun writeHeartRate(heartList: List<HeartRate>)
}