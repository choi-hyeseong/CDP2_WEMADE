package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.sign.repository.UserRepository
import com.home.cdp2app.user.sign.usecase.LoginUseCase
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class LoginUseCaseTest {

    private val repository : UserRepository = mockk()
    private val loginUseCase = LoginUseCase(repository)

    @Test
    fun TEST_LOGIN() {
        //요청 호출되는지만 확인
        val email : CapturingSlot<String> = slot()
        val password : CapturingSlot<String> = slot()

        coEvery { repository.login(capture(email), capture(password)) } returns mockk()
        runBlocking {
            loginUseCase("knu_email", "knu_pass") //수행
            assertEquals("knu_email", email.captured)
            assertEquals("knu_pass", password.captured)
        }
    }
}