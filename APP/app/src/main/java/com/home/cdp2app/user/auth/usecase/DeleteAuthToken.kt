package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.repository.AuthRepository

/**
 * AuthToken을 제거하는 usecase
 */
class DeleteAuthToken(private val repository: AuthRepository) {

    /**
     * repository에서 auth token을 제거함
     * @see AuthRepository.removeToken
     */
    suspend operator fun invoke() {
        repository.removeToken()
    }
}