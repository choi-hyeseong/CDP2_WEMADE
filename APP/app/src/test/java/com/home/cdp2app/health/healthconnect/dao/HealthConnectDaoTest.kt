package com.home.cdp2app.health.healthconnect.dao

import android.content.Context
import android.os.Build
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.powermock.reflect.Whitebox
import java.time.Instant
import java.time.ZoneOffset

class HealthConnectDaoTest {

    //해당 client로 insert 요청이 들어가는지 확인.
    @Test
    fun TEST_INSERT_RECORD() {
        //static & companion mock. 동적 테스트 하기엔 실제 health connect에 기록이 들어감.
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        Whitebox.setInternalState(
            Build.VERSION::class.java,
            "SDK_INT",
            32) //reflection api 이용해서 version 수정

        //input mock
        val input = slot<List<Record>>()

        //getorcreate로 반환되는 obj
        val mockClient = mockk<HealthConnectClient>()
        every { HealthConnectClient.getOrCreate(any()) } returns mockClient
        //input만 보기위해 return mock
        coEvery { mockClient.insertRecords(capture(input)) } returns mockk()

        val healthConnectDao = HealthConnectDao(mockk())
        val record = HeartRateRecord(
            Instant.now(), ZoneOffset.UTC, Instant.now(), ZoneOffset.UTC, listOf(
                HeartRateRecord.Sample(Instant.now(), 75)
            ))
        runBlocking { healthConnectDao.insertRecord(record) }
        coVerify(atLeast = 1) { mockClient.insertRecords(any()) }
        //slot으로 캡처된거랑 일치한지 확인
        assertEquals(75, (input.captured[0] as HeartRateRecord).samples[0].beatsPerMinute)
        unmockkAll()
    }

    @Test
    fun TEST_READ_RECORD() {
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 32)

        val mockClient = mockk<HealthConnectClient>()
        every { HealthConnectClient.getOrCreate(any()) } returns mockClient
        val readRecordsRequest = ReadRecordsRequest(
            HeartRateRecord::class,
            TimeRangeFilter.Companion.before(Instant.now()))

        //response mock
        val response = mockk<ReadRecordsResponse<HeartRateRecord>>()
        every { response.records } returns listOf(
            HeartRateRecord(
                Instant.now(), ZoneOffset.UTC, Instant.now(), ZoneOffset.UTC, listOf(
                    HeartRateRecord.Sample(Instant.now(), 75),
                    HeartRateRecord.Sample(Instant.now(), 155)
                )))
        //read시 mock된 response 반환
        coEvery { mockClient.readRecords(readRecordsRequest) } returns response

        val healthConnectDao = HealthConnectDao(mockk())

        val result = runBlocking { healthConnectDao.readRecord(readRecordsRequest) }

        assertEquals(155, result[0].samples[1].beatsPerMinute)
        coVerify(atLeast = 1) { mockClient.readRecords(readRecordsRequest) }
        unmockkAll()
    }
}