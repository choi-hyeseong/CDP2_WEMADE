package com.home.cdp2app.user.sign.repository

import com.home.cdp2app.user.sign.api.dto.LoginResponseDTO
import com.home.cdp2app.user.sign.api.dto.RegisterResponse
import com.skydoves.sandwich.ApiResponse

/**
 * 유저 정보를 관리하는 레포지토리
 */
interface UserRepository {

    /**
     * 로그인을 수행합니다.
     * @param email 로그인 id로 사용되는 이메일입니다.
     * @param password 로그인 비밀번호로 사용됩니다.
     * @return API 요청 결과를 LoginResponseDTO(token)값과 함께 반환합니다. HttpStatus..
     */
    suspend fun login(email : String, password : String) : ApiResponse<LoginResponseDTO>

    /**
     * 회원가입을 수행합니다.
     * @param email 회원가입 id로 사용되는 이메일입니다.
     * @param password 회원가입 비밀번호로 사용됩니다.
     * @param nickname 닉네임입니다.
     * @return API 요청 결과를 RegisterResponse와 함께 반환합니다. HttpStatus..
     */
    suspend fun register(email : String, password : String, nickname : String) : ApiResponse<RegisterResponse>
}