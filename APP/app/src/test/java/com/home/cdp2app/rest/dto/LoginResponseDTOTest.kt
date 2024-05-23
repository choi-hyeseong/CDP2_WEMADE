package com.home.cdp2app.rest.dto

import com.home.cdp2app.user.sign.api.dto.LoginResponseDTO
import com.home.cdp2app.user.token.entity.AuthToken
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class LoginResponseDTOTest {

    @Test
    fun TEST_TO_ENTITY() {
        val dto = LoginResponseDTO("ACCESS", "REFRESH")
        assertEquals(AuthToken("ACCESS", "REFRESH"), dto.toEntity()) //data class이므로 값비교
    }
}