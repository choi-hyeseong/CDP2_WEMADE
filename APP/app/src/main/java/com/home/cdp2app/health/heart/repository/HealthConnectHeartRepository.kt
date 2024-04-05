package com.home.cdp2app.health.heart.repository

import com.home.cdp2app.health.heart.dao.HealthConnectHeartDao
import com.home.cdp2app.health.heart.entity.HeartRate
import java.time.Instant

class HealthConnectHeartRepository(private val healthConnectHeartDao: HealthConnectHeartDao) : HeartRepository {
    override suspend fun readHeartRate(start: Instant, end: Instant): List<HeartRate> {
        return healthConnectHeartDao.readHeartRate(start, end)
    }

    override suspend fun writeHeartRate(heartList: List<HeartRate>) {
        healthConnectHeartDao.writeHeartRate(heartList)
    }

}