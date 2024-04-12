package com.home.cdp2app.health.bloodpressure.mapper

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.units.Pressure
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.sleep.entity.SleepHour
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime

//혈압 엔티티 : 레코드 1:1 매핑 테스트
class BloodPressureMapperTest {

    private val bloodPressureMapper = BloodPressureMapper()

    @Test
    fun TEST_MAP_TO_ENTITY() {
        val record = BloodPressureRecord(Instant.now(), ZonedDateTime.now().offset, Pressure.millimetersOfMercury(120.0), Pressure.millimetersOfMercury(80.0)) //120/80의 혈압을 방금 측정한 레코드
        val mappedEntity = bloodPressureMapper.mapToEntity(record)
        assertNotNull(mappedEntity) //결과값이 notnull
        assertEquals(record.time, mappedEntity.date) //측정시간 일치 여부
        assertEquals(120.0, mappedEntity.systolic, 0.0) //수축기 혈압 일치여부
        assertEquals(80.0, mappedEntity.diastolic, 0.0) //이완기 혈압 일치여부
    }

    @Test
    fun TEST_MAP_TO_RECORD() {
        val entity = BloodPressure(Instant.now(), 125.0, 75.0) //125/75의 혈압을 방금 측정한 엔티티
        val record = bloodPressureMapper.mapToRecord(entity)
        assertNotNull(record) //결과값이 notnull
        assertEquals(entity.date, record.time) //측정시간 일치
        assertEquals(entity.diastolic, record.diastolic.inMillimetersOfMercury) //이완기 혈압 일치여부
        assertEquals(entity.systolic, record.systolic.inMillimetersOfMercury) //수축기혈압 일치 여부
    }
}