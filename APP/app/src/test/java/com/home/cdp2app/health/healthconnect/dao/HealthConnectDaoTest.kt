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

    val mockClient = mockk<HealthConnectClient>()

    //정적 메소드를 mock 하기 위한 메소드. 사용후 반드시 releaseMock 사용할것.
    fun handleStaticMock() {
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        Whitebox.setInternalState(
            Build.VERSION::class.java,
            "SDK_INT",
            32) //reflection api 이용해서 version 수정
        //getOrCreate시 mockClient 반환.
        every { HealthConnectClient.getOrCreate(any()) } returns mockClient
    }

    fun releaseMock() {
        unmockkAll()
    }

    // insertRecord 수행시 insertRecords(listof(record))와 동일하게 동작하는지 확인
    @Test
    fun TEST_INSERT_RECORD() {
        handleStaticMock()

        //input capture용 slot
        val input = slot<List<Record>>()

        //input만 보기위해 return mock
        coEvery { mockClient.insertRecords(capture(input)) } returns mockk()

        val healthConnectDao = HealthConnectDao(mockk())
        //input
        val record = HeartRateRecord(
            Instant.now(), ZoneOffset.UTC, Instant.now(), ZoneOffset.UTC, listOf(
                HeartRateRecord.Sample(Instant.now(), 75)
            ))
        runBlocking { healthConnectDao.insertRecord(record) }
        //mockClient의 record 삽입된지 확인
        coVerify(atLeast = 1) { mockClient.insertRecords(any()) }
        //slot으로 캡처된게 listOf(record)한것과 동일한지 체크
        assertEquals(listOf(record), input.captured)
        releaseMock()
    }

    //해당 client로 insert 요청이 들어가는지 확인.
    @Test
    fun TEST_INSERT_RECORDS() {
        //static & companion mock. 동적 테스트 하기엔 실제 health connect에 기록이 들어감.
        handleStaticMock()

        //input capture용 slot
        val input = slot<List<Record>>()

        //input만 보기위해 return mock
        coEvery { mockClient.insertRecords(capture(input)) } returns mockk()

        val healthConnectDao = HealthConnectDao(mockk())
        //input
        val record = HeartRateRecord(
            Instant.now(), ZoneOffset.UTC, Instant.now(), ZoneOffset.UTC, listOf(
                HeartRateRecord.Sample(Instant.now(), 75)
            ))
        val recordSecond = HeartRateRecord(
            Instant.now(), ZoneOffset.UTC, Instant.now(), ZoneOffset.UTC, listOf(
                HeartRateRecord.Sample(Instant.now(), 75)
            ))
        runBlocking { healthConnectDao.insertRecords(listOf(record, recordSecond)) }

        coVerify(atLeast = 1) { mockClient.insertRecords(any()) }
        //slot으로 캡처된거랑 일치한지 확인
        assertEquals(2, input.captured.size)
        assertEquals(record, input.captured[0])
        assertEquals(recordSecond, input.captured[1])
        releaseMock()
    }

    @Test
    fun TEST_READ_RECORD_BEFORE() {
        handleStaticMock()
        val targetDate = Instant.now()



        //response mock
        val response = mockk<ReadRecordsResponse<HeartRateRecord>>()
        every { response.records } returns listOf(
            HeartRateRecord(
                Instant.now(), ZoneOffset.UTC, Instant.now(), ZoneOffset.UTC, listOf(
                    HeartRateRecord.Sample(Instant.now(), 75),
                    HeartRateRecord.Sample(Instant.now(), 155)
                )))

        //read시 mock된 response 반환
        val readRecordsRequest = ReadRecordsRequest(
            HeartRateRecord::class,
            TimeRangeFilter.Companion.before(targetDate))
        coEvery { mockClient.readRecords(readRecordsRequest) } returns response

        val healthConnectDao = HealthConnectDao(mockk())

        val result = runBlocking { healthConnectDao.readRecordBefore(HeartRateRecord::class, targetDate) }

        assertEquals(155, result[0].samples[1].beatsPerMinute)
        coVerify(atLeast = 1) { mockClient.readRecords(readRecordsRequest) }
        releaseMock()
    }

    @Test
    fun TEST_READ_RECORD_BETWEEN() {
       handleStaticMock()

        val targetDate = Instant.now().minusMillis(1000)
        val endDate = Instant.now()



        val readRecordsRequest = ReadRecordsRequest(
            HeartRateRecord::class,
            TimeRangeFilter.Companion.between(targetDate, endDate))
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

        val result = runBlocking { healthConnectDao.readRecordBetween(HeartRateRecord::class, targetDate, endDate) }

        assertEquals(155, result[0].samples[1].beatsPerMinute)
        coVerify(atLeast = 1) { mockClient.readRecords(readRecordsRequest) }
        releaseMock()
    }

    @Test
    fun TEST_READ_RECORD_AFTER() {
       handleStaticMock()

        val targetDate = Instant.now().minusMillis(1000)
        //실제 요청될 request
        val readRecordsRequest = ReadRecordsRequest(
            HeartRateRecord::class,
            TimeRangeFilter.Companion.after(targetDate))

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

        val result = runBlocking { healthConnectDao.readRecordAfter(HeartRateRecord::class, targetDate) }

        assertEquals(155, result[0].samples[1].beatsPerMinute)
        coVerify(atLeast = 1) { mockClient.readRecords(readRecordsRequest) }
        releaseMock()
    }
}