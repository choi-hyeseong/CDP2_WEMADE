package com.home.cdp2app.user.auth.repository

import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.memory.exception.TargetNotFoundException
import com.home.cdp2app.user.auth.entity.AuthToken
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow


class PreferenceAuthRepositoryTest {

    private val storage : SharedPreferencesStorage = mockk() //mock storage
    private val repository : PreferenceAuthRepository = PreferenceAuthRepository(storage)

    @Test
    fun TEST_LOAD_TOKEN() {
        val key : CapturingSlot<String> = slot()
        val response = AuthToken("ACCESS", "REFRESH")
        coEvery { storage.loadObject(capture(key), AuthToken::class) } returns response //response 반환 및 capture

        runBlocking {
            val result = assertDoesNotThrow { repository.getToken() }
            coVerify(atLeast = 1) { storage.loadObject(any(), AuthToken::class) }
            assertEquals(response, result)
            assertEquals("AUTH_TOKEN", key.captured)
        }
    }

    @Test
    fun TEST_SAVE_TOKEN() {
        val key : CapturingSlot<String> = slot()
        val value : CapturingSlot<AuthToken> = slot() //save slot
        val target = AuthToken("ACCESS", "REFRESH")
        coEvery { storage.saveObject(capture(key), capture(value)) } returns true //response 반환 및 capture

        runBlocking {
            assertDoesNotThrow { repository.saveToken(target) }
            coVerify(atLeast = 1) { storage.saveObject(any(), any())}
            assertEquals(target, value.captured)
            assertEquals("AUTH_TOKEN", key.captured)
        }
    }

    @Test
    fun TEST_DELETE_KEY() {
        // key delete check
        val key : CapturingSlot<String> = slot()
        coEvery { storage.delete(capture(key)) } returns true
        runBlocking {
            repository.removeToken()
            assertEquals("AUTH_TOKEN", key.captured)
            coVerify(atLeast = 1) { storage.delete(any()) }
        }
    }

    @Test
    fun TEST_HAVE_TOKEN() {
        //token을 가진경우
        coEvery { storage.loadObject(any(), AuthToken::class) } returns AuthToken("access", "refresh")
        runBlocking {
            assertTrue(repository.hasToken())
        }
    }

    @Test
    fun TEST_NOT_HAVE_TOKEN_SERIALIZE() {
        //token을 역직렬화 하지 못하는경우
        coEvery { storage.loadObject(any(), AuthToken::class) } throws IllegalArgumentException("역직렬화 불가")
        runBlocking {
            assertFalse(repository.hasToken())
        }
    }


    @Test
    fun TEST_NOT_FOUND_TOKEN() {
        //token이 없는경우
        coEvery { storage.loadObject(any(), AuthToken::class) } throws TargetNotFoundException("토큰이 없음")
        runBlocking {
            assertFalse(repository.hasToken())
        }
    }




}