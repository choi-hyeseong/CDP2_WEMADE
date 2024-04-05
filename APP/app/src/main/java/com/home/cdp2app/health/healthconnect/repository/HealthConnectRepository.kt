package com.home.cdp2app.health.healthconnect.repository

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.healthconnect.dao.HealthDao


class HealthConnectRepository(private val healthConnectDao: HealthConnectDao) : HealthRepository {
    override suspend fun insertRecord(record: Record) {
        healthConnectDao.insertRecord(record)
    }

    override suspend fun <T : Record> readRecord(request: ReadRecordsRequest<T>): List<T> {
        return healthConnectDao.readRecord(request)
    }
}