package com.home.cdp2app.user.auth.usecase

import android.graphics.Paint.Cap
import com.home.cdp2app.user.auth.entity.AuthToken
import com.home.cdp2app.user.auth.repository.AuthRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SaveAuthTokenTest {
    private val repository: AuthRepository = mockk() //mock
    private val saveAuthToken = SaveAuthToken(repository)

    @Test
    fun TEST_HAS_TOKEN() {
        //요청 전달되는지만 확인
        val captureToken : CapturingSlot<AuthToken> = slot()
        val request = AuthToken("ACCESS", "REF")
        coEvery { repository.saveToken(capture(captureToken)) } returns mockk() //capture
        runBlocking {
            saveAuthToken(request)
            coVerify(atLeast = 1) { repository.saveToken(any()) } //수행됐는지 확인
            assertEquals(request, captureToken.captured)
        }

    }
}