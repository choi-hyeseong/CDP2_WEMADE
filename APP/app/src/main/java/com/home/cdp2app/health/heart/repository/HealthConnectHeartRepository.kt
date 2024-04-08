package com.home.cdp2app.health.heart.repository

import androidx.health.connect.client.records.HeartRateRecord
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import java.time.Instant

//HealthConnect를 이용한 심박수를 다루는 레포. HealthConnectDao를 이용해서 접근함
class HealthConnectHeartRepository(private val healthConnectDao: HealthConnectDao, private val heartRateMapper: HeartRateMapper) : HeartRepository {
    override suspend fun readHeartRateBefore(date: Instant): List<HeartRate> {
        //dao로 읽어온 record를 mapper를 통해 변환 후, flatten하여 하나의 리스트로 변환.
        //우선 before을 통해 모든 데이터를 읽어옴. 추후 기간을 정할 필요가 있을때 repo에 추가 기능 구현 예정
        return healthConnectDao.readRecordBefore(HeartRateRecord::class, date).flatMap { heartRateMapper.mapToEntity(it) }

    }

    override suspend fun writeHeartRate(heartList: List<HeartRate>) {
        healthConnectDao.insertRecord(heartRateMapper.mapToRecord(heartList))
    }

}