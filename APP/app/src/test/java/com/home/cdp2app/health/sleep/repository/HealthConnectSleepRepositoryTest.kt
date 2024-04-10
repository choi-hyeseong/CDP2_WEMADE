package com.home.cdp2app.health.sleep.repository

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.SleepSessionRecord
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime

class HealthConnectSleepRepositoryTest {

    val dao: HealthConnectDao = mockk()
    val mapper: SleepHourMapper = SleepHourMapper()
    val repository = HealthConnectSleepRepository(mapper, dao)

    @Test
    fun TEST_READ_RECORD() {
        val targetDate = Instant.now().minusSeconds(10) //10초간의 수면시간
        val offset = ZonedDateTime.now().offset
        val record = SleepSessionRecord(targetDate, offset, Instant.now(), offset) //response record

        coEvery {
            dao.readRecordBefore(
                SleepSessionRecord::class,
                any())
        } returns listOf(record)  //record 반환하게
        runBlocking {
            val response = repository.readSleepHoursBefore(targetDate)
            coVerify(atLeast = 1) {
                dao.readRecordBefore(
                    SleepSessionRecord::class,
                    any())
            } //dao에서 요청이 들어갔는지
            assertEquals(1, response.size)
            //결과값 일치하는지 확인
            assertEquals(targetDate, response[0].date)
            assertEquals(10, response[0].duration.seconds)
        }
    }

    @Test
    fun TEST_READ_RECORDS() {
        val offset = ZonedDateTime.now().offset
        //result가 2개인지, 각각 5,7 초의 수면시간을 보증하는지 확인
        val records = listOf(
            SleepSessionRecord(
                Instant.now().minusSeconds(5),
                offset,
                Instant.now(),
                offset) //response record
            , SleepSessionRecord(Instant.now().minusSeconds(7), offset, Instant.now(), offset)
        )

        coEvery {
            dao.readRecordBefore(
                SleepSessionRecord::class,
                any())
        } returns records //record 반환하게
        runBlocking {
            val response =
                repository.readSleepHoursBefore(Instant.now()) //입력값 검증이 아닌, Duration만 보면 되므로 아무값 사용
            coVerify(atLeast = 1) {
                dao.readRecordBefore(
                    SleepSessionRecord::class,
                    any())
            } //dao에서 요청이 들어갔는지
            assertEquals(2, response.size)
            //결과값 일치하는지 확인
            assertEquals(5, response[0].duration.seconds)
            assertEquals(7, response[1].duration.seconds)
        }
    }

    @Test
    fun TEST_WRITE_RECORD() {
        val entities = listOf(
            SleepHour(Instant.now(), Duration.ofSeconds(2)),
            SleepHour(Instant.now(), Duration.ofSeconds(5))) //2초, 5초간의 수면시간을 가지는 레코드
        val input = slot<List<Record>>() //dao에 요청가는지 확인하기 위한 slot

        coEvery { dao.insertRecords(capture(input)) } returns mockk() //Unit만 반환하면됨
        runBlocking {
            repository.writeSleepHours(entities)
            coVerify(atLeast = 1) {
                dao.insertRecords(any())
            } //dao에서 요청이 들어갔는지

            //결과값 일치하는지 확인
            assertEquals(2, input.captured.size)
            input.captured.map { it as SleepSessionRecord }.forEachIndexed { index, record ->
                assertEquals(entities[index], mapper.mapToEntity(record))
            }

        }
    }
}