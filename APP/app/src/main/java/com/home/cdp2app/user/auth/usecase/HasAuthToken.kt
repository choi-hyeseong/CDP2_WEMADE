package com.home.cdp2app.user.auth.usecase

import com.home.cdp2app.user.auth.repository.AuthRepository

/**
 * AuthToken을 가지고 있는지 확인하는 유스케이스
 */
class HasAuthToken(private val repository: AuthRepository) {

    /**
     * 레포지토리에서 AuthToken을 가지고 있는지 여부를 반환합니다.
     * @see AuthRepository.hasToken
     * @return true일경우 저장되어 있고, false인경우 저장된 값이 없습니다.
     */
    suspend operator fun invoke() : Boolean {
        return repository.hasToken()
    }
}