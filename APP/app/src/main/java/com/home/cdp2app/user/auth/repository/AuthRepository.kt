package com.home.cdp2app.user.auth.repository

import com.home.cdp2app.memory.exception.TargetNotFoundException
import com.home.cdp2app.user.auth.entity.AuthToken

/**
 * AuthToken을 관리하는 레포지토리 입니다.
 */
interface AuthRepository {

    /**
     * AuthToken을 저장하는 클래스 입니다.
     * @param authToken 저장할 토큰입니다.
     */
    suspend fun saveToken(authToken: AuthToken)

    /**
     * 저장된 AuthToken을 가져옵니다.
     * @throws TargetNotFoundException 저장된 토큰이 없을경우 발생합니다.
     * @throws IllegalArgumentException 해당 토큰을 불러올 수 없는경우 발생합니다.
     * @return AuthToken을 반환합니다.
     */
    suspend fun getToken() : AuthToken

    /**
     * 저장된 Token을 제거합니다. (invalidate)
     */
    suspend fun removeToken()

    /**
     * AuthToken이 저장되어 있는지 확인합니다. try - catch문으로 getToken이 잘 수행되는지 확인하는 방식입니다.
     * @return 저장되어 있을경우 true, 아닌경우 false를 반환합니다.
     */
    suspend fun hasToken() : Boolean {
        return kotlin.runCatching { getToken() }.isSuccess
    }
}