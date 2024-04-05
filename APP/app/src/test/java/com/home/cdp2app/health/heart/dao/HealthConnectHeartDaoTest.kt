package com.home.cdp2app.health.heart.dao

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

import java.time.Instant
import java.time.ZonedDateTime

class HealthConnectHeartDaoTest {

    @Test
    fun TEST_READ_HEART_RATE() {
        //실질적으로 HealthConnect 접근하는 dao mock
        val mockDao = mockk<HealthConnectDao>()
        val mapper = HeartRateMapper() //record - entity
        val dao = HealthConnectHeartDao(mockDao, mapper)
        val start = Instant.now().minusMillis(1000)
        val end = Instant.now()
        val offset = ZonedDateTime.now().offset

        //mock dao에서 접근하는 요청
        val request = ReadRecordsRequest(HeartRateRecord::class, TimeRangeFilter.Companion.between(start, end))
        //그에따라 받아올 값
        val response = listOf(HeartRateRecord(start, offset, end, offset, listOf(
            HeartRateRecord.Sample(Instant.now(), 150), HeartRateRecord.Sample(Instant.now(), 100)
        )))
        coEvery { mockDao.readRecord(request) } returns response
        runBlocking {
            val entityResponse = dao.readHeartRate(start, end)
            assertEquals(2, entityResponse.size)
            assertEquals(150, entityResponse[0].bpm)
            assertEquals(100, entityResponse[1].bpm)
            coVerify(atLeast = 1) { mockDao.readRecord(request)}
        }

    }

    @Test
    fun TEST_WRITE_HEART_RATE() {
        val mockDao = mockk<HealthConnectDao>()
        val mapper = HeartRateMapper() //record - entity
        val dao = HealthConnectHeartDao(mockDao, mapper)
        val firstTime = Instant.now().minusMillis(500)
        val secondTime = Instant.now().minusMillis(100)
        val entities = listOf(HeartRate(firstTime, 150), HeartRate(secondTime, 100)) //entity 값들

        val inputSlot = slot<Record>() //HealthConnectDao로 요청받는 input 검증
        val record = mapper.mapToRecord(entities) //실제 변환될것으로 예상되는 record
        coEvery { mockDao.insertRecord(capture(inputSlot)) } returns mockk()
        runBlocking {
            dao.writeHeartRate(entities)
            coVerify(atLeast = 1) { mockDao.insertRecord(any()) }
            assertEquals(inputSlot.captured, record) //dao로 요청된 값과 변환된 값 같은지 검증

        }
    }
}