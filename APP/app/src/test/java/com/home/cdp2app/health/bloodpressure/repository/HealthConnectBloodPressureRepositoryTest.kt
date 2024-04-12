package com.home.cdp2app.health.bloodpressure.repository

import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.units.Pressure
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.time.ZonedDateTime

class HealthConnectBloodPressureRepositoryTest {

    private val dao: HealthConnectDao = mockk() //dao mock
    private val mapper = BloodPressureMapper()
    private val repository = HealthConnectBloodPressureRepository(dao, mapper)

    @Test
    fun TEST_READ_RECORD() {
        val offset = ZonedDateTime.now().offset
        val mapper = BloodPressureMapper() //결과값 비교를 위한 매퍼
        val records = listOf(
            BloodPressureRecord(Instant.now().minusSeconds(2), offset, Pressure.millimetersOfMercury(130.0), Pressure.millimetersOfMercury(80.0)), //2초전 측정한 130/80 혈압
            BloodPressureRecord(Instant.now(), offset, Pressure.millimetersOfMercury(100.0), Pressure.millimetersOfMercury(70.0)) //방금 측정한 100/70 혈압
        )

        coEvery { dao.readRecordBefore(BloodPressureRecord::class, any()) } returns records //혈압 레코드를 읽을시 record 반환
        runBlocking {
            val response = repository.readBloodPressureBefore(Instant.now())
            assertEquals(2, response.size) //size 일치한지
            response.forEachIndexed { index, pressure ->
                //내용물이 매핑된 결과와 일치한지 비교
                assertEquals(mapper.mapToEntity(records[index]), pressure)
            }
            coVerify(atLeast = 1) { dao.readRecordBefore(BloodPressureRecord::class, any()) } //readBefore 요청이 1번이상 발생했는가
        }

    }

    @Test
    fun TEST_WRITE_RECORD() {
        val input: CapturingSlot<BloodPressureRecord> = slot() //input값 캡처를 위한 slot
        val mapper = BloodPressureMapper() //결과값 비교를 위한 매퍼
        coEvery { dao.insertRecord(capture(input)) } returns mockk() //slot 캡처를 위한 coEvery 및 unit반환

        val entity = BloodPressure(Instant.now(), 127.0, 70.0) //127/70의 혈압을 방금 측정한 엔티티
        runBlocking {
            repository.writeBloodPressure(entity)
            val mappedRecord = mapper.mapToRecord(entity) //결과 비교를 위한 매핑
            assertEquals(mappedRecord, input.captured) //결과값 비교
            coVerify(atLeast = 1) { dao.insertRecord(any()) } //insertRecord가 호출된지 확인
        }

    }
}