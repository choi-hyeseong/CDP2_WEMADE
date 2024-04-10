package com.home.cdp2app.health.sleep.mapper

import androidx.health.connect.client.records.SleepSessionRecord
import com.home.cdp2app.health.sleep.entity.SleepHour
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime

class SleepHourMapperTest {

    val sleepHourMapper = SleepHourMapper()

    //1초간의 수면시간을 가진 Record 생성
    fun buildRecord() : SleepSessionRecord {
        val start = Instant.now().minusMillis(1000)
        val end = Instant.now()
        val offset = ZonedDateTime.now().offset
        return SleepSessionRecord(start, offset, end, offset)
    }

    @Test
    fun TEST_MAP_TO_ENTITY() {
        val record = buildRecord()
        val mappedEntity = sleepHourMapper.mapToEntity(record)
        assertEquals(record.startTime, mappedEntity.date) //수면 시작시간 일치 여부 확인
        assertEquals(1, mappedEntity.duration.seconds) //수면시간 일치 여부
    }

    @Test
    fun TEST_MAP_TO_RECORD() {
        val sleepHour = SleepHour(Instant.now(), Duration.ofSeconds(1)) //1초의 수면시간을 가지는 엔티티
        val mappedRecord = sleepHourMapper.mapToRecord(sleepHour)
        assertEquals(sleepHour.date, mappedRecord.startTime) //시작시간 일치 여부
        assertEquals(1, mappedRecord.endTime.epochSecond - mappedRecord.startTime.epochSecond) //수면시간 일치 여부
        assertEquals(sleepHour.date.plusSeconds(1), mappedRecord.endTime) //종료시간 일치 여부
    }

}