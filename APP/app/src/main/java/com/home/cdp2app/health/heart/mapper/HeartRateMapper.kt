package com.home.cdp2app.health.heart.mapper

import androidx.health.connect.client.records.HeartRateRecord
import com.home.cdp2app.health.healthconnect.mapper.MultipleRecordMapper
import com.home.cdp2app.health.heart.entity.HeartRate
import java.time.ZonedDateTime

//심박수 record를 mapping하는 클래스
class HeartRateMapper : MultipleRecordMapper<HeartRateRecord, HeartRate> {
    override fun mapToEntity(record: HeartRateRecord): List<HeartRate> {
        return record.samples.map {
            HeartRate(it.time, it.beatsPerMinute)
        }
    }

    override fun mapToRecord(entities: List<HeartRate>): HeartRateRecord {
        if (entities.isEmpty())
            throw IllegalArgumentException("HeartRate Entity List는 비어있어선 안됩니다.")
        //zone의 offset은 지역이 일정하므로 현재 지역의 offset을 가져옴
        val zoneOffset = ZonedDateTime.now().offset
        //시작시간 - entity 목록중 제일 이른 시간
        val startTime = entities.minBy { it.time }.time
        //종료시간 - entity 목록중 제일 마지막 시간
        val endTime = entities.maxBy { it.time }.time
        //sample로 매핑된 entity
        val samples = entities.map { HeartRateRecord.Sample(it.time, it.bpm) }
        return HeartRateRecord(startTime, zoneOffset, endTime, zoneOffset, samples)
    }
}