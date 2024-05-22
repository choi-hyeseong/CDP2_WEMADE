package com.home.cdp2app.user.auth.entity

import com.home.cdp2app.user.auth.token.entity.AuthToken
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class AuthTokenTest {

    @Test
    fun TEST_CONVERT_TO_HEADER_TOKEN() {
        // header에 사용될 토큰으로 전환되는지 확인
        val token = AuthToken("ACCESS", "REFERSH")
        assertEquals("Bearer ACCESS", token.getHeaderAccessToken())
    }
}