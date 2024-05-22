package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.sign.repository.UserRepository
import com.home.cdp2app.user.auth.sign.usecase.RegisterUseCase
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class RegisterUseCaseTest {
    private val repository : UserRepository = mockk()
    private val registerUseCase = RegisterUseCase(repository)

    @Test
    fun TEST_LOGIN() {
        //요청 호출되는지만 확인
        val email : CapturingSlot<String> = slot()
        val password : CapturingSlot<String> = slot()
        val nickname : CapturingSlot<String> = slot()

        coEvery { repository.register(capture(email), capture(password), capture(nickname)) } returns mockk()
        runBlocking {
            registerUseCase("knu_email", "knu_pass", "knu_nick") //수행
            assertEquals("knu_email", email.captured)
            assertEquals("knu_pass", password.captured)
            assertEquals("knu_nick", nickname.captured)
        }
    }
}