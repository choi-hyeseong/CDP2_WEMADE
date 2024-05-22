package com.home.cdp2app.tutorial.repository

import com.home.cdp2app.common.memory.SharedPreferencesStorage

/**
 * SharedPreference를 사용해서 Tutorial 여부를 저장하는 레포지토리
 * @property preferencesStorage tutorial 여부를 관리하는 preference 입니다.
 */
class PreferenceTutorialRepository(private val preferencesStorage: SharedPreferencesStorage) : TutorialRepository {

    private val KEY : String = "TUTORIAL"

    override suspend fun checkCompleted(): Boolean {
        return preferencesStorage.getBoolean(KEY, false)
    }

    override suspend fun setCompleted() {
        preferencesStorage.putBoolean(KEY, true)
    }


}