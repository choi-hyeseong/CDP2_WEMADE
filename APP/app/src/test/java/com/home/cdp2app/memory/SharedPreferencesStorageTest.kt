package com.home.cdp2app.memory

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Paint.Cap
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.memory.exception.TargetNotFoundException
import com.home.cdp2app.util.json.JsonMapperUtil
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.time.Instant

class SharedPreferencesStorageTest {

    private val mockContext : Context = mockk() //application context mock
    private lateinit var preferencesStorage : SharedPreferencesStorage //test
    private lateinit var sharedPreferences: SharedPreferences  //composition preference
    private var editor: Editor = mockk()

    @Before
    fun PROVIDE_PREFERENCE() {
        sharedPreferences = mockk()
        every { mockContext.getSharedPreferences(any(), any()) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        preferencesStorage = SharedPreferencesStorage(mockContext)
    }

    @Test
    fun TEST_SAVE_OBJECT() {
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<String> = slot()
        //capture
        every { editor.putString(capture(key), capture(value)) } returns editor
        every { editor.commit() } returns true
        val record = HeartRate(Instant.now(), 149L)
        runBlocking {
            Assert.assertTrue(preferencesStorage.saveObject("RECORD_SAVE", record))
            Assert.assertEquals(JsonMapperUtil.writeToString(record), value.captured)
            Assert.assertEquals("RECORD_SAVE", key.captured)
            verify(atLeast = 1) { editor.putString(any(), any()) }
        }
    }


    @Test
    fun TEST_DELETE_OBJECT() {
        val key : CapturingSlot<String> = slot()
        coEvery { editor.remove(capture(key)) } returns editor
        coEvery { editor.commit() } returns true

        runBlocking {
            val result = preferencesStorage.delete("TEST_KEY")
            assertTrue(result)
            assertEquals("TEST_KEY", key.captured)
            coVerify(atLeast = 1) { editor.remove(any()) }
        }
    }

    @Test
    fun TEST_LOAD_OBJECT() {
        val key : CapturingSlot<String> = slot()
        val bloodPressure = BloodPressure(Instant.now().minusSeconds(150), 170.0, 100.0)
        //capture
        every { sharedPreferences.contains(any()) } returns true
        every { sharedPreferences.getString(capture(key), any()) } returns JsonMapperUtil.writeToString(bloodPressure)
        runBlocking {
            val record = preferencesStorage.loadObject("RECORD_SAVE", BloodPressure::class)
            Assert.assertEquals(bloodPressure, record)
            Assert.assertEquals("RECORD_SAVE", key.captured)
            verify(atLeast = 1) { sharedPreferences.getString(any(), any()) }
        }
    }

    @Test
    fun TEST_THROW_EMPTY_KEY_ON_LOAD() {
        //키에 저장된 값이 없을경우 exception
        every { sharedPreferences.contains(any()) } returns false
        runBlocking {
            assertThrows<TargetNotFoundException> {
                preferencesStorage.loadObject("Test", HeartRate::class)
            }
        }
    }

    @Test
    fun TEST_THROW_NOT_STRING_VALUE() {
        //string 형식으로 저장되지 않았을경우 throw
        every { sharedPreferences.contains(any()) } returns true
        every { sharedPreferences.getString(any(), any()) } returns null
        runBlocking {
            assertThrows<TargetNotFoundException> {
                preferencesStorage.loadObject("Test", HeartRate::class)
            }
        }
    }

    @Test
    fun TEST_CAN_NOT_DESERIALIZE() {
        val record = HeartRate(Instant.now(), 145L)
        every { sharedPreferences.contains(any()) } returns true
        every { sharedPreferences.getString(any(), any()) } returns JsonMapperUtil.writeToString(record)
        runBlocking {
            assertThrows<IllegalArgumentException> {
                //잘못된 deserialize
                preferencesStorage.loadObject("TEST", BloodPressure::class)
            }
        }
    }

    @Test
    fun TEST_SAVE_INT() {
        //입력값 검증
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<Int> = slot()
        every { editor.putInt(capture(key), capture(value)) } returns editor
        every { editor.commit() } returns true

        runBlocking {
            val result = preferencesStorage.putInt("TEST", 3)
            Assert.assertEquals("TEST", key.captured)
            Assert.assertEquals(3, value.captured)
            Assert.assertTrue(result)
            coVerify(atLeast = 1) { editor.commit() }
        }
    }

    @Test
    fun TEST_GET_INT() {
        //입력값 검증
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<Int> = slot()
        every { sharedPreferences.getInt(capture(key), capture(value)) } returns 3 //3반환

        runBlocking {
            val result = preferencesStorage.getInt("TEST", 2)
            Assert.assertEquals("TEST", key.captured)
            Assert.assertEquals(2, value.captured)
            Assert.assertEquals(result, 3)
        }
    }

    @Test
    fun TEST_SAVE_DOUBLE() {
        //입력값 검증
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<Float> = slot()
        every { editor.putFloat(capture(key), capture(value)) } returns editor
        every { editor.commit() } returns true

        runBlocking {
            val result = preferencesStorage.putDouble("TEST", 3.0)
            Assert.assertEquals("TEST", key.captured)
            Assert.assertEquals(3.0f, value.captured, 0.0f)
            Assert.assertTrue(result)
            coVerify(atLeast = 1) { editor.commit() }
        }
    }

    @Test
    fun TEST_GET_DOUBLE() {
        //입력값 검증
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<Float> = slot()
        every { sharedPreferences.getFloat(capture(key), capture(value)) } returns 3.0f //3반환

        runBlocking {
            val result = preferencesStorage.getDouble("TEST", 2.0)
            Assert.assertEquals("TEST", key.captured)
            Assert.assertEquals(2.0f, value.captured, 0.0f)
            Assert.assertEquals(result, 3.0, 0.0)
        }
    }

    @Test
    fun TEST_SAVE_BOOLEAN() {
        //입력값 검증
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<Boolean> = slot()
        every { editor.putBoolean(capture(key), capture(value)) } returns editor
        every { editor.commit() } returns true

        runBlocking {
            val result = preferencesStorage.putBoolean("TEST", true)
            Assert.assertEquals("TEST", key.captured)
            Assert.assertTrue(value.captured)
            Assert.assertTrue(result)
            coVerify(atLeast = 1) { editor.commit() }
        }
    }

    @Test
    fun TEST_GET_BOOLEAN() {
        //입력값 검증
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<Boolean> = slot()
        every { sharedPreferences.getBoolean(capture(key), capture(value)) } returns false //false반환

        runBlocking {
            val result = preferencesStorage.getBoolean("TEST", true)
            Assert.assertEquals("TEST", key.captured)
            Assert.assertTrue(value.captured) //default value false
            Assert.assertFalse(result)
        }
    }


}