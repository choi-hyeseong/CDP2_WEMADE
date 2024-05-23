package com.home.cdp2app.user.auth.repository

import com.home.cdp2app.user.sign.api.RemoteUserAPI
import com.home.cdp2app.user.sign.api.dto.LoginRequestDTO
import com.home.cdp2app.user.sign.api.dto.LoginResponseDTO
import com.home.cdp2app.user.sign.api.dto.RegisterRequestDTO
import com.home.cdp2app.user.sign.api.dto.RegisterResponse
import com.home.cdp2app.user.sign.repository.RemoteUserRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Response

class RemoteUserRepositoryTest {

    private val userAPI : RemoteUserAPI = mockk()
    private val repository : RemoteUserRepository = RemoteUserRepository(userAPI)

    @Test
    fun TEST_LOGIN() {
        val requestSlot : CapturingSlot<LoginRequestDTO> = slot()
        coEvery { userAPI.login(capture(requestSlot)) } returns ApiResponse.of { Response.success(LoginResponseDTO("Auth", "Refresh")) }//success

        runBlocking {
            val response = repository.login("email", "password")
            var successTestBoolean = false

            assertEquals("email", requestSlot.captured.email)
            assertEquals("password", requestSlot.captured.password)
            response.onSuccess {
                // onSuccess 부분 통과하는지 확인하기 위한 구문
                if ("Auth" == data.accessToken && "Refresh" == data.refreshToken)
                    successTestBoolean = true
            }
            assertTrue(successTestBoolean)
        }
    }

    @Test
    fun TEST_Register() {
        val requestSlot : CapturingSlot<RegisterRequestDTO> = slot()
        coEvery { userAPI.register(capture(requestSlot)) } returns ApiResponse.of { Response.success(RegisterResponse()) }//success

        runBlocking {
            val response = repository.register("email", "password", "nickname")
            var successTestBoolean = false

            assertEquals("email", requestSlot.captured.email)
            assertEquals("password", requestSlot.captured.password)
            assertEquals("nickname", requestSlot.captured.nickname)
            response.onSuccess {
                // onSuccess 부분 통과하는지 확인하기 위한 구문. 회원가입은 아마 Statuscode로만 비교하면 되서 그냥 이렇게 해도 될듯
                successTestBoolean = true
            }
            assertTrue(successTestBoolean)
        }
    }
}