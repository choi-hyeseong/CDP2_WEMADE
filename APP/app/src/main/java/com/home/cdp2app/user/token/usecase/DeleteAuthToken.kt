package com.home.cdp2app.user.token.usecase

import com.home.cdp2app.user.token.repository.AuthTokenRepository

/**
 * AuthToken을 제거하는 usecase
 */
class DeleteAuthToken(private val repository: AuthTokenRepository) {

    /**
     * repository에서 auth token을 제거함
     * @see AuthTokenRepository.removeToken
     */
    suspend operator fun invoke() {
        repository.removeToken()
    }
}