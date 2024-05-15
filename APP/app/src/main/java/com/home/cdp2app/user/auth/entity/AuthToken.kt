package com.home.cdp2app.user.auth.entity

/**
 * REST API 통신시 JWT에 사용되는 토큰 엔티티 입니다.
 * @property accessToken Authentication 헤더에 들어가는 값입니다. 액세스에 사용됩니다.
 * @property refreshToken accessToken이 만료됐을경우 새로고침시 사용됩니다.
 */
data class AuthToken(
        val accessToken : String,
        val refreshToken : String
)