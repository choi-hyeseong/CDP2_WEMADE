package com.home.cdp2app.user.sign.api

import com.home.cdp2app.user.sign.api.dto.LoginRequestDTO
import com.home.cdp2app.user.sign.api.dto.LoginResponseDTO
import com.home.cdp2app.user.sign.api.dto.RegisterRequestDTO
import com.home.cdp2app.user.sign.api.dto.RegisterResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 로그인, 회원가입 API 요청하는 Retrofit interface
 */
interface RemoteUserAPI {

    /**
     * 회원가입을 수행합니다.
     * @param registerRequestDTO 회원가입에 필요한 양식입니다. (이메일, 비밀번호, 닉네임)
     * @return Api Response와 함께 회원가입 결과를 반환합니다.
     */
    @POST("/auth/register/email")
    suspend fun register(@Body registerRequestDTO: RegisterRequestDTO) : ApiResponse<RegisterResponse>

    /**
     * 로그인을 수행합니다.
     * @param loginRequestDTO 로그인에 필요한 양식입니다. (이메일, 비밀번호)
     * @return Api Response와 함께 로그인 결과로 jwt 토큰 (Access, Refresh)를 받습니다.
     */
    @POST("/auth/login/email")
    suspend fun login(@Body loginRequestDTO: LoginRequestDTO) : ApiResponse<LoginResponseDTO>
}