package com.home.cdp2app.user.token.repository

import com.home.cdp2app.common.memory.LocalDataStorage
import com.home.cdp2app.user.token.entity.AuthToken

/**
 * SharedPreference를 사용하는 AuthToken Repository, 보안이 필요하다면 EncryptedStorage로 변경하면 될듯
 */
class PreferenceAuthTokenRepository(private val storage: LocalDataStorage) : AuthTokenRepository {

    private val KEY : String = "AUTH_TOKEN"

    override suspend fun saveToken(authToken: AuthToken) {
        storage.saveObject(KEY, authToken)
    }

    override suspend fun getToken(): AuthToken {
        return storage.loadObject(KEY, AuthToken::class)
    }

    override suspend fun removeToken() {
        storage.delete(KEY)
    }

}