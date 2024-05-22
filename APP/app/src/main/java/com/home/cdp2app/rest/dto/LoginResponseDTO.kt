package com.home.cdp2app.rest.dto

import com.home.cdp2app.user.token.entity.AuthToken

/**
 * 로그인 응답에 사용되는 DTO 입니다.
 */
data class LoginResponseDTO(val accessToken : String, val refreshToken : String) {

    //auth token entity로 변환하는 메소드
    fun toEntity() : AuthToken = AuthToken(accessToken, refreshToken)
}
