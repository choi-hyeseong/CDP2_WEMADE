package com.home.cdp2app.health.heart.dao

import android.content.Context
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import java.time.Instant

//health connect를 사용하므로 healthconnect dao를 이용해서 접근. 따로 dao의 형태로 구현한건 추후 다른 provider를 사용할 수 있기 때문에.
class HealthConnectHeartDao(private val healthConnectDao: HealthConnectDao,
                            private val heartRateMapper: HeartRateMapper) : HeartDao {

    //list로 읽은 heartrate record를 mapper를 통해 List로 변경하고, 이를 flatMap으로 해서 합침
    override suspend fun readHeartRate(start: Instant, end: Instant): List<HeartRate> {
        return healthConnectDao.readRecord(
            ReadRecordsRequest(
                HeartRateRecord::class,
                TimeRangeFilter.Companion.between(start, end))
        ).flatMap {
            heartRateMapper.mapToEntity(it)
        }
    }

    override suspend fun writeHeartRate(heartList: List<HeartRate>) {
        healthConnectDao.insertRecord(heartRateMapper.mapToRecord(heartList))
    }
}