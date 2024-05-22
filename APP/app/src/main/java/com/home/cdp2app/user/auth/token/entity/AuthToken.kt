package com.home.cdp2app.user.auth.token.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * REST API 통신시 JWT에 사용되는 토큰 엔티티 입니다.
 * @property accessToken Authentication 헤더에 들어가는 값입니다. 액세스에 사용됩니다.
 * @property refreshToken accessToken이 만료됐을경우 새로고침시 사용됩니다.
 */
@JsonIgnoreProperties("headerAccessToken") //<- 왜 function을 직렬화해서 저장..?
data class AuthToken(
        val accessToken : String,
        val refreshToken : String
) {

    /**
     * Header에 사용하기 위한 AccessToken 가져오기
     * @return Bearer $accessToken 형식의 문자열로 리턴됩니다.
     */
    fun getHeaderAccessToken() : String = "Bearer $accessToken"
}