package com.home.cdp2app.health.healthconnect.dao

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest

//실제 healthconnect와의 접근이 이루어지는 dao. context는 반드시 application context를 주입할것.
class HealthConnectDao(context: Context) : HealthDao {

    //lazy로 선언해두어 실제 접근이 일어날때 초기화 됨. (permission, sdk status check 고려)
    private val healthConnectClient : HealthConnectClient by lazy {  HealthConnectClient.getOrCreate(context) }

    override suspend fun insertRecord(record: Record) {
        healthConnectClient.insertRecords(listOf(record))
    }

    override suspend fun <T : Record> readRecord(request: ReadRecordsRequest<T>): List<T> {
        return healthConnectClient.readRecords(request).records
    }
}