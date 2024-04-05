package com.home.cdp2app.health.heart.dao

import com.home.cdp2app.health.heart.entity.HeartRate
import java.time.Instant

//실제 심박수를 읽는 dao. health connect 외에 다른 provider를 사용할 수 있으므로 interface로 분리
interface HeartDao {

    suspend fun readHeartRate(start : Instant, end : Instant) : List<HeartRate>

    suspend fun writeHeartRate(heartList : List<HeartRate>)

}