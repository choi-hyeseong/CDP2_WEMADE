package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.token.repository.AuthTokenRepository
import com.home.cdp2app.user.auth.token.usecase.DeleteAuthToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteAuthTokenTest {
    private val repository: AuthTokenRepository = mockk() //mock
    private val deleteAuthToken = DeleteAuthToken(repository)

    @Test
    fun TEST_HAS_TOKEN() {
        //요청 전달되는지만 확인
        coEvery { repository.removeToken() } returns mockk()
        runBlocking {
            deleteAuthToken()
            coVerify(atLeast = 1) { repository.removeToken() } //수행됐는지 확인
        }

    }
}
