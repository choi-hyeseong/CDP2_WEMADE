package com.home.cdp2app.user.auth.token.usecase

import com.home.cdp2app.user.auth.token.entity.AuthToken
import com.home.cdp2app.user.auth.token.repository.AuthTokenRepository

/**
 * AuthToken을 저장하는 유스케이스
 */
class SaveAuthToken(private val authTokenRepository: AuthTokenRepository) {

    /**
     * AuthToken을 저장하는 메소드 입니다.
     * @param authToken 저장할 토큰입니다.
     * @see AuthTokenRepository.saveToken
     */
    suspend operator fun invoke(authToken: AuthToken) {
        authTokenRepository.saveToken(authToken)
    }
}