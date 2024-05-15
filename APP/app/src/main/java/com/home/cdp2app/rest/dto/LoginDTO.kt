package com.home.cdp2app.rest.dto

/**
 * 로그인에 사용되는 DTO 입니다.
 */
data class LoginDTO(private val accessToken : String, private val refreshToken : String)