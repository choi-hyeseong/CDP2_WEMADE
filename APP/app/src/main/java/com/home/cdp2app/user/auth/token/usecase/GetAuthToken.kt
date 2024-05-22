package com.home.cdp2app.user.auth.token.usecase

import com.home.cdp2app.user.auth.token.entity.AuthToken
import com.home.cdp2app.user.auth.token.repository.AuthTokenRepository

/**
 * AuthToken을 가져오는 유스케이스
 * @property authTokenRepository 토큰을 가져올 레포지토리 입니다.
 */
class GetAuthToken(private val authTokenRepository: AuthTokenRepository) {

    /**
     * 저장된 토큰을 가져옵니다.
     * @return AuthToken을 반환합니다.
     * @see AuthTokenRepository.getToken
     */
    suspend operator fun invoke() : AuthToken {
        return authTokenRepository.getToken()
    }
}