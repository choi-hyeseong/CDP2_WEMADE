package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.token.repository.AuthTokenRepository
import com.home.cdp2app.user.token.usecase.HasAuthToken
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class HasAuthTokenTest {

    private val repository: AuthTokenRepository = mockk() //mock
    private val hasAuthToken = HasAuthToken(repository)

    @Test
    fun TEST_HAS_TOKEN() {
        //요청 전달되는지만 확인
        coEvery { repository.hasToken() } returns true
        runBlocking {
            assertTrue(hasAuthToken())
        }

    }
}