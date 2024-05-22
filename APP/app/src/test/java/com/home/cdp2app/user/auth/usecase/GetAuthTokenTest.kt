package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.token.entity.AuthToken
import com.home.cdp2app.user.auth.token.repository.AuthTokenRepository
import com.home.cdp2app.user.auth.token.usecase.GetAuthToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class GetAuthTokenTest {

    private val repository : AuthTokenRepository = mockk()
    private val getAuthToken = GetAuthToken(repository)

    @Test
    fun TEST_GET_TOKEN() {
        coEvery { getAuthToken() } returns AuthToken("ACCESS", "")

        runBlocking {
            val result = getAuthToken()

            coVerify(atLeast = 1) { getAuthToken() }
            assertEquals("ACCESS", result.accessToken)
        }
    }
}