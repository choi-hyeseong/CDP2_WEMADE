package com.home.cdp2app.health.heart.repository


import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.time.Instant
import java.time.ZonedDateTime

class HealthConnectHeartRepositoryTest {
    val heartDao = mockk<HealthConnectDao>()
    val mapper = HeartRateMapper()

    //dao의 readRecord를 mock
    @Test
    fun TEST_READ_RECORD() {
        //given
        val heartRepository: HeartRepository = HealthConnectHeartRepository(heartDao, mapper)
        //실질적으로 HealthConnect 접근하는 dao mock
        val start = Instant.now().minusMillis(1000)
        val end = Instant.now()
        val offset = ZonedDateTime.now().offset

        //mock dao에서 접근하는 요청
        //그에따라 받아올 값
        val response = listOf(
            HeartRateRecord(
                start, offset, end, offset, listOf(
                    HeartRateRecord.Sample(Instant.now(), 150), HeartRateRecord.Sample(Instant.now(), 100))))
        coEvery { heartDao.readRecordBefore(HeartRateRecord::class, any()) } returns response
        runBlocking {
            val entityResponse = heartRepository.readHeartRateBefore(start)
            Assertions.assertEquals(2, entityResponse.size)
            Assertions.assertEquals(150, entityResponse[0].bpm)
            Assertions.assertEquals(100, entityResponse[1].bpm)
            coVerify(atLeast = 1) { heartDao.readRecordBefore(HeartRateRecord::class, any()) }
        }

    }

    //dao의 insertRecord를 mock
    @Test
    fun TEST_WRITE_RECORD() {
        val heartRepository: HeartRepository = HealthConnectHeartRepository(heartDao, mapper)
        val firstTime = Instant.now().minusMillis(500)
        val secondTime = Instant.now().minusMillis(100)
        val entities = listOf(HeartRate(firstTime, 150), HeartRate(secondTime, 100)) //entity 값들

        val inputSlot = slot<Record>() //HealthConnectDao로 요청받는 input 검증
        val record = mapper.mapToRecord(entities) //실제 변환될것으로 예상되는 record
        coEvery { heartDao.insertRecord(capture(inputSlot)) } returns mockk()
        runBlocking {
            heartRepository.writeHeartRate(entities)
            coVerify(atLeast = 1) { heartDao.insertRecord(any()) }
            Assertions.assertEquals(inputSlot.captured, record) //dao로 요청된 값과 변환된 값 같은지 검증

        }

    }
}
