package com.home.cdp2app.health.sleep.repository

import androidx.health.connect.client.records.SleepSessionRecord
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import java.time.Instant

//HealthConnect Repository 구현체
class HealthConnectSleepRepository(private val sleepHourMapper: SleepHourMapper, private val dao: HealthConnectDao) : SleepRepository {
    override suspend fun readSleepHoursBefore(date: Instant): List<SleepHour> {
        return dao.readRecordBefore(SleepSessionRecord::class, date).map { sleepHourMapper.mapToEntity(it) }
    }

    //SleepHour로 받아서 1:1로 구현하는 dao.insertRecord를 써도 되지만, List도 지원하기 위해..
    override suspend fun writeSleepHours(data: List<SleepHour>) {
        return dao.insertRecords(data.map { sleepHourMapper.mapToRecord(it) })
    }
}