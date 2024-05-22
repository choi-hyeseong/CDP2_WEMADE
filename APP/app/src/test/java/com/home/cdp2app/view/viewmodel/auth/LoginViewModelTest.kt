package com.home.cdp2app.view.viewmodel.auth

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.rest.dto.LoginResponseDTO
import com.home.cdp2app.rest.type.NetworkStatus
import com.home.cdp2app.user.token.entity.AuthToken
import com.home.cdp2app.user.sign.usecase.LoginUseCase
import com.home.cdp2app.user.token.usecase.SaveAuthToken
import com.home.cdp2app.user.sign.validator.LoginValidator
import com.home.cdp2app.user.sign.view.login.viewmodel.LoginViewModel
import com.home.cdp2app.valid.type.ValidateStatus
import com.skydoves.sandwich.ApiResponse
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class LoginViewModelTest {

    // constructor mock
    private val loginValidator = LoginValidator()
    private val loginUseCase : LoginUseCase = mockk()
    private val saveAuthToken : SaveAuthToken = mockk()

    private val viewModel = LoginViewModel(loginValidator, loginUseCase, saveAuthToken)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    @Test
    fun TEST_VALIDATE_FAIL() {
        // validate fail시 livedata 확인
        viewModel.login("asdf@knu", "qwer1234!")
        val result = viewModel.validateLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = result.getContent()
        assertNotNull(content)
        assertEquals(ValidateStatus.VALUE_ERROR, content)
    }

    @Test
    fun TEST_LOGIN_SUCCESS() {
        // capture slot
        val emailSlot : CapturingSlot<String> = slot()
        val passSlot : CapturingSlot<String> = slot()
        val tokenSlot : CapturingSlot<AuthToken> = slot()
        val response = LoginResponseDTO("ACCESS_TOKEN", "REFRESH_TOKEN")
        val successApiResponse = ApiResponse.of { Response.success(response) } //api response

        coEvery { saveAuthToken(capture(tokenSlot)) } returns mockk() // token 저장되는지 확인
        coEvery { loginUseCase(capture(emailSlot), capture(passSlot)) } returns successApiResponse // success 호출

        viewModel.login("KNU@email.com", "qwer1234!")
        // coroutine이므로 sleep 처리
        Thread.sleep(500)
        val status = viewModel.loginStatusLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = status.getContent()

        assertEquals("KNU@email.com", emailSlot.captured)
        assertEquals("qwer1234!", passSlot.captured)

        assertNotNull(content)
        assertEquals(NetworkStatus.OK, content) //OK 호출 된지 확인
        coVerify(atLeast = 1) { saveAuthToken(any()) } //save 된지
        assertEquals(response.toEntity(), tokenSlot.captured)

    }

    @Test
    fun TEST_LOGIN_FAIL_ON_ERROR() {
        // bad request의 response error 생성
        val response = ApiResponse.Failure.Error(Response.error<LoginResponseDTO>(400, ResponseBody.create("application/json".toMediaTypeOrNull()!!, "")))
        mockkStatic(Log::class) //로그 mock
        val logSlot : CapturingSlot<String> = slot() //로그 메시지 슬롯
        every { Log.w(any(), capture(logSlot)) } returns 1 //로그 메시지 capture
        coEvery { loginUseCase(any(), any()) } returns response
        viewModel.login("qwer@knu.ac.kr", "qwer1234!") //로그인 validate 통과

        assertThrows(TimeoutException::class.java) { viewModel.validateLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) } //livedata에 값 셋팅 안되야함
        val result = viewModel.loginStatusLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //status 가져오기
        val content = result.getContent()
        assertNotNull(content)
        assertEquals(NetworkStatus.BAD_REQUEST, content) //bad request로 컨버팅 된지 확인
        assertTrue(logSlot.captured.contains("Encountered Retrofit Error. Status : ${response.statusCode}")) //status code를 log에 입력하는지 확인
        unmockkAll()
    }

    @Test
    fun TEST_LOGIN_FAIL_ON_EXCEPTION() {
        val response = ApiResponse.error<LoginResponseDTO>(IOException("연결 종료"))
        mockkStatic(Log::class) //로그 mock
        val logSlot : CapturingSlot<String> = slot() //로그 메시지 슬롯
        every { Log.w(any(), capture(logSlot)) } returns 1 //로그 메시지 capture
        coEvery { loginUseCase(any(), any()) } returns response
        viewModel.login("qwer@knu.ac.kr", "qwer1234!") //로그인 validate 통과

        assertThrows(TimeoutException::class.java) { viewModel.validateLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) } //livedata에 값 셋팅 안되야함
        val result = viewModel.loginStatusLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //status 가져오기
        val content = result.getContent()
        assertNotNull(content)
        assertEquals(NetworkStatus.CONNECTION_ERROR, content) //연결 오류로 컨버팅 된지 확인 (IO Exception은 connection error로 변경됨)
        assertTrue(logSlot.captured.contains("Encountered Retrofit Exception")) //status code를 log에 입력하는지 확인
        unmockkAll()
    }
}