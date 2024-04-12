package com.home.cdp2app.health.bloodpressure.mapper

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.units.Pressure
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.healthconnect.mapper.SingleRecordMapper
import java.time.ZonedDateTime

/**
 * BlodPressureRecord를 BloodPressure 엔티티로 매핑하는 클래스
 */
class BloodPressureMapper : SingleRecordMapper<BloodPressureRecord, BloodPressure> {
    override fun mapToEntity(record: BloodPressureRecord): BloodPressure {
        return BloodPressure(record.time, record.systolic.inMillimetersOfMercury, record.diastolic.inMillimetersOfMercury)
    }

    override fun mapToRecord(entity: BloodPressure): BloodPressureRecord {
        return BloodPressureRecord(entity.date, ZonedDateTime.now().offset, Pressure.millimetersOfMercury(entity.systolic), Pressure.millimetersOfMercury(entity.diastolic))
    }
}