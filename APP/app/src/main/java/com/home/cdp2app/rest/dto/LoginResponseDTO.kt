package com.home.cdp2app.rest.dto

/**
 * 로그인 응답에 사용되는 DTO 입니다.
 */
data class LoginResponseDTO(private val accessToken : String, private val refreshToken : String)