package com.home.cdp2app.user.auth.repository

import com.home.cdp2app.rest.api.RemoteUserAPI
import com.home.cdp2app.rest.dto.LoginRequestDTO
import com.home.cdp2app.rest.dto.LoginResponseDTO
import com.home.cdp2app.rest.dto.RegisterRequestDTO
import com.home.cdp2app.rest.dto.RegisterResponse
import com.skydoves.sandwich.ApiResponse

// remote user api retrofit 요청을 사용하는 레포지토리
class RemoteUserRepository(private val remoteUserAPI: RemoteUserAPI) : UserRepository {

    override suspend fun login(email: String, password: String): ApiResponse<LoginResponseDTO> {
        return remoteUserAPI.login(LoginRequestDTO(email, password))
    }

    override suspend fun register(email: String, password: String, nickname: String): ApiResponse<RegisterResponse> {
        return remoteUserAPI.register(RegisterRequestDTO(email, password, nickname))
    }

}