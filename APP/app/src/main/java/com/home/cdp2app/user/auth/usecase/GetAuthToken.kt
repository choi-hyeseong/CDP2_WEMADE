package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.entity.AuthToken
import com.home.cdp2app.user.auth.repository.AuthTokenRepository

class GetAuthToken(private val authTokenRepository: AuthTokenRepository) {

    suspend operator fun invoke() : AuthToken {
        return authTokenRepository.getToken()
    }
}