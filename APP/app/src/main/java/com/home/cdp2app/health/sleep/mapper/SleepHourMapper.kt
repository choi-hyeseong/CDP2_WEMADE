package com.home.cdp2app.health.sleep.mapper

import androidx.health.connect.client.records.SleepSessionRecord
import com.home.cdp2app.health.healthconnect.mapper.SingleRecordMapper
import com.home.cdp2app.health.sleep.entity.SleepHour
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * 수면시간 - 레코드 매핑 클래스.
 */
class SleepHourMapper : SingleRecordMapper<SleepSessionRecord, SleepHour> {
    /**
     * Record - Entity 매핑
     * @param record 수면시간 레코드 입니다.
     * @return SleepHour 엔티티입니다. 레코드의 종료시간 - 시작시간을 수행한 Duration을 반환합니다.
     */
    override fun mapToEntity(record: SleepSessionRecord): SleepHour {
        return SleepHour(record.startTime, Duration.ofSeconds(record.endTime.epochSecond - record.startTime.epochSecond))
    }

    override fun mapToRecord(entity: SleepHour): SleepSessionRecord {
        val startTime = entity.date
        val offset = ZonedDateTime.now().offset
        val endTime = entity.date.plusSeconds(entity.duration.seconds)
        return SleepSessionRecord(startTime, offset, endTime, offset)
    }
}