package com.home.cdp2app.health.heart.repository

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import java.time.Instant

//HealthConnect를 이용한 심박수를 다루는 레포. HealthConnectDao를 이용해서 접근함
class HealthConnectHeartRepository(private val healthConnectDao: HealthConnectDao, private val heartRateMapper: HeartRateMapper) : HeartRepository {
    override suspend fun readHeartRate(start: Instant, end: Instant): List<HeartRate> {
        //dao로 읽어온 record를 mapper를 통해 변환 후, flatten하여 하나의 리스트로 변환.
        return healthConnectDao.readRecord(ReadRecordsRequest(HeartRateRecord::class, TimeRangeFilter.Companion.between(start, end))).flatMap { heartRateMapper.mapToEntity(it) }

    }

    override suspend fun writeHeartRate(heartList: List<HeartRate>) {
        healthConnectDao.insertRecord(heartRateMapper.mapToRecord(heartList))
    }

}