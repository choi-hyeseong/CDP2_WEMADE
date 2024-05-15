package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.entity.AuthToken
import com.home.cdp2app.user.auth.repository.AuthRepository

/**
 * AuthToken을 저장하는 유스케이스
 */
class SaveAuthToken(private val authRepository: AuthRepository) {

    /**
     * AuthToken을 저장하는 메소드 입니다.
     * @param authToken 저장할 토큰입니다.
     * @see AuthRepository.saveToken
     */
    suspend operator fun invoke(authToken: AuthToken) {
        authRepository.saveToken(authToken)
    }
}