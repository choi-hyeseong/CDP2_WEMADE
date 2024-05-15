package com.home.cdp2app.rest.api

import com.home.cdp2app.rest.dto.LoginRequestDTO
import com.home.cdp2app.rest.dto.LoginResponseDTO
import com.home.cdp2app.rest.dto.RegisterRequestDTO
import com.home.cdp2app.rest.dto.RegisterResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteUserAPI {

    @POST("/auth/register/email")
    suspend fun register(@Body registerRequestDTO: RegisterRequestDTO) : ApiResponse<RegisterResponse>

    @POST("/auth/login/email")
    suspend fun login(@Body loginRequestDTO: LoginRequestDTO) : ApiResponse<LoginResponseDTO>
}