package com.home.cdp2app.health.basic.repository

import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.memory.SharedPreferencesStorage

class PreferenceBasicInfoRepository(private val sharedPreferencesStorage: SharedPreferencesStorage) : BasicInfoRepository {

    private val preferenceKey: String = "BASIC_INFO" //sharedPreference에서 사용할 key값

    override suspend fun loadInfo(loadDefault: Boolean): BasicInfo {
        val result: Result<BasicInfo> = kotlin.runCatching {
            //sharedPreference에서 값을 가져옴. runCatching을 통해 Result로 파싱
            sharedPreferencesStorage.loadObject(preferenceKey, BasicInfo::class)
        }
        return if (result.isSuccess) result.getOrNull()!! //result가 성공했을경우 notNull이므로 리턴
        else if (loadDefault) BasicInfoRepository.DEFAULT //실패했을때 default가 선언되어 있으면 Default 반환
        else throw result.exceptionOrNull()!! //아닌경우 exception 던짐
    }

    override suspend fun saveInfo(info: BasicInfo) {
        sharedPreferencesStorage.saveObject(preferenceKey, info)
    }

    //로드로 불러온값이 DEFAULT와 동일한지 주소값 비교. 같을경우 저장안된값, false로 load 요청하고 exception 체크해도 됨
    override suspend fun hasInfo(): Boolean {
        return loadInfo(true) !== BasicInfoRepository.DEFAULT
    }

}