package com.home.cdp2app.user.tutorial.repository

import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.tutorial.repository.PreferenceTutorialRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class PreferenceTutorialRepositoryTest {

    private val storage : SharedPreferencesStorage = mockk() //mock
    private val repository = PreferenceTutorialRepository(storage)

    @Test
    fun TEST_LOAD_TUTORIAL() {
        val keySlot : CapturingSlot<String> = slot() //key capture
        val valueSlot : CapturingSlot<Boolean> = slot()
        coEvery { storage.getBoolean(capture(keySlot), capture(valueSlot)) } returns false

        runBlocking {
            val result = repository.checkCompleted()
            coVerify(atLeast = 1) { storage.getBoolean(any(), any()) } //호출 됐는지
            assertFalse(result) //로딩 안됨
            assertFalse(valueSlot.captured) //기본값 false
            assertEquals("TUTORIAL", keySlot.captured)
        }
    }

    @Test
    fun TEST_SAVE_TUTORIAL() {
        val keySlot : CapturingSlot<String> = slot() //key capture
        val valueSlot : CapturingSlot<Boolean> = slot()
        coEvery { storage.putBoolean(capture(keySlot), capture(valueSlot)) } returns true

        runBlocking {
            repository.setCompleted() //저장
            coVerify(atLeast = 1) { storage.putBoolean(any(), any()) }
            assertEquals("TUTORIAL", keySlot.captured)
            assertTrue(valueSlot.captured)
        }
    }
}