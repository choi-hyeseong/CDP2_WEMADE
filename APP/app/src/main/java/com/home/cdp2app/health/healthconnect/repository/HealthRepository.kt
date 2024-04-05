package com.home.cdp2app.health.healthconnect.repository

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest

//healthconnect에 접근하기 위한 레포지토리. 실질적 접근은 dao를 통해서.
interface HealthRepository {

    //health connect record 삽입
    suspend fun insertRecord(record: Record)

    //record 기록 읽어오기.
    suspend fun <T : Record> readRecord(request : ReadRecordsRequest<T>) : List<T>
}