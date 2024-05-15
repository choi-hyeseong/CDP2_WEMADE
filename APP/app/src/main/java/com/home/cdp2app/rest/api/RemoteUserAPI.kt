package com.home.cdp2app.rest.api

import com.home.cdp2app.rest.dto.LoginDTO
import com.skydoves.sandwich.ApiResponse

interface RemoteUserAPI {

    suspend fun register() : ApiResponse<Boolean>

    suspend fun login() : ApiResponse<LoginDTO>
}