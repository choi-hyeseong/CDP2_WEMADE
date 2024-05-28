package com.home.cdp2app.health.bloodpressure.repository

import androidx.health.connect.client.records.BloodPressureRecord
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import java.time.Instant

/**
 * HealthConnect 기반 BloodPressureRepository
 */
class HealthConnectBloodPressureRepository(private val dao: HealthConnectDao, private val mapper: BloodPressureMapper) : BloodPressureRepository {
    override suspend fun readBloodPressureBefore(date: Instant): List<BloodPressure> {
        return dao.readRecordBefore(BloodPressureRecord::class, date).map { mapper.mapToEntity(it) }
    }

    override suspend fun writeBloodPressure(pressure: BloodPressure) {
        dao.insertRecord(mapper.mapToRecord(pressure))
    }
}