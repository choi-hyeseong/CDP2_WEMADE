package com.home.cdp2app.health.healthconnect.dao

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest

interface HealthDao {

    suspend fun insertRecord(record: Record)

    suspend fun <T : Record> readRecord(request: ReadRecordsRequest<T>): List<T>
}