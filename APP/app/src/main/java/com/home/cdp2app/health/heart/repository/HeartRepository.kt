package com.home.cdp2app.health.heart.repository

import com.home.cdp2app.health.heart.entity.HeartRate
import java.time.Instant

//심박수를 읽기 위한 repository
interface HeartRepository {

    suspend fun readHeartRate(start : Instant, end : Instant) : List<HeartRate>

    suspend fun writeHeartRate(heartList : List<HeartRate>)
}