package com.home.cdp2app.util.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.home.cdp2app.common.util.json.JsonMapperUtil
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Instant

class JsonMapperUtilTest {

    @Test
    fun TEST_READ_INVALID_JSON() {
        //processing으로도 핸들링 되는것 같음
        org.junit.jupiter.api.assertThrows<JsonProcessingException> {
            JsonMapperUtil.readObject("<<>>", HeartRate::class)
        }
    }

    @Test
    fun TEST_NOT_CONSTRUCT_OBJECT() {
        //mapping으로도 exception catching이 되는듯
        org.junit.jupiter.api.assertThrows<JsonMappingException> {
            JsonMapperUtil.readObject(JsonMapperUtil.writeToString(HeartRate(Instant.now(), 150)), BloodPressure::class)
        }
    }

    @Test
    fun TEST_SUCCESS_LOAD_OBJECT() {
        val origin = HeartRate(Instant.now().minusSeconds(150), 140L)
        val recordString = JsonMapperUtil.writeToString(origin)
        assertDoesNotThrow {
            val record = JsonMapperUtil.readObject(recordString, HeartRate::class)
            Assert.assertEquals(origin.time, record.time)
            Assert.assertEquals(origin.bpm, record.bpm)
        }
    }

    @Test
    fun TEST_WRITE_OBJECT() {
        //json의 key value로 변경되는지 확인
        Assert.assertEquals("{\"1\":\"3\"}", JsonMapperUtil.writeToString(mapOf("1" to "3")))
    }


}