package com.home.cdp2app.health.healthconnect.dao

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import kotlin.reflect.KClass

//실제 healthconnect와의 접근이 이루어지는 dao. context는 반드시 application context를 주입할것.
class HealthConnectDao(context: Context) : HealthDao {

    //lazy로 선언해두어 실제 접근이 일어날때 초기화 됨. (permission, sdk status check 고려)
    private val healthConnectClient : HealthConnectClient by lazy {  HealthConnectClient.getOrCreate(context) }

    //기본 구현체를 insertRecords로 하고, Interface에서 listOf 하는 형식으로 1개 insert하게 지원.
    override suspend fun insertRecords(record: List<Record>) {
        healthConnectClient.insertRecords(record)
    }

    //T를 넣는게 아니라 recordClass를 넣어야 하는게 맞음..
    override suspend fun <T : Record> readRecordBetween(recordClass: KClass<T>, start: Instant, end: Instant): List<T> {
        return readRecord(ReadRecordsRequest(recordClass, TimeRangeFilter.Companion.between(start, end)))
    }

    override suspend fun <T : Record> readRecordBefore(recordClass: KClass<T>, date: Instant): List<T> {
       return readRecord(ReadRecordsRequest(recordClass, TimeRangeFilter.Companion.before(date)))
    }

    override suspend fun <T : Record> readRecordAfter(recordClass: KClass<T>, date: Instant): List<T> {
       return readRecord(ReadRecordsRequest(recordClass, TimeRangeFilter.Companion.after(date)))
    }

    private suspend fun <T : Record> readRecord(request: ReadRecordsRequest<T>): List<T> {
        return healthConnectClient.readRecords(request).records
    }
}