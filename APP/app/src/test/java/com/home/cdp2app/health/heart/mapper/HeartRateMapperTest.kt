package com.home.cdp2app.health.heart.mapper

import androidx.health.connect.client.records.HeartRateRecord
import com.home.cdp2app.health.heart.entity.HeartRate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.time.ZonedDateTime

class HeartRateMapperTest {

    val heartRateMapper = HeartRateMapper()


    fun buildRecord(samples: List<HeartRateRecord.Sample>): HeartRateRecord {
        val start = Instant.now()
        val end = Instant.now()
        val offset = ZonedDateTime.now().offset
        return HeartRateRecord(start, offset, end, offset, samples)
    }

    @Test
    fun TEST_MAP_TO_ENTITY() {
        val samples = listOf(
            HeartRateRecord.Sample(Instant.now(), 140), HeartRateRecord.Sample(
                Instant.now(), 130))
        val record = buildRecord(samples)
        val mappedEntity = heartRateMapper.mapToEntity(record)
        assertEquals(2, mappedEntity.size)
        assertEquals(samples[0].time, mappedEntity[0].time)
        assertEquals(samples[0].beatsPerMinute, mappedEntity[0].bpm)
    }

    //비어있는 경우 entity map
    @Test
    fun TEST_EMPTY_SAMPLES_MAP_TO_ENTITY() {
        val record = buildRecord(listOf())
        val mappedEntity = heartRateMapper.mapToEntity(record)
        assertEquals(0, mappedEntity.size)
    }

    @Test
    fun TEST_MAP_TO_RECORD() {
        val firstTime = Instant.now().minusMillis(10)
        val secondTime = Instant.now().minusMillis(5)
        val entities = listOf(HeartRate(firstTime, 100), HeartRate(secondTime, 150))
        val mappedRecord = heartRateMapper.mapToRecord(entities)
        assertEquals(firstTime, mappedRecord.startTime)
        assertEquals(secondTime, mappedRecord.endTime)
        assertEquals(2, mappedRecord.samples.size)
        assertEquals(150, mappedRecord.samples[1].beatsPerMinute)
        assertEquals(secondTime, mappedRecord.samples[1].time)
    }

    @Test
    fun TEST_EMPTY_ENTITIES_MAP_TO_RECORD() {
        val entities: List<HeartRate> = listOf()
        assertThrows<IllegalArgumentException> { heartRateMapper.mapToRecord(entities) }
    }

    @Test
    fun TEST_ONLY_ONE_MAP_TO_RECORD() {
        val firstTime = Instant.now().minusMillis(10)
        val entities = listOf(HeartRate(firstTime, 100))
        val mappedRecord = heartRateMapper.mapToRecord(entities)
        assertEquals(firstTime, mappedRecord.startTime)
        assertEquals(1, mappedRecord.samples.size)
        assertEquals(100, mappedRecord.samples[0].beatsPerMinute)
    }
}