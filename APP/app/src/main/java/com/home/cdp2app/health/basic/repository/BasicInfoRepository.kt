package com.home.cdp2app.health.basic.repository

import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.health.basic.type.Gender

/**
 * 사용자의 기본 건강정보를 관리하는 레포지토리 입니다.
 * @property DEFAULT 저장된 건강정보가 없을경우 불러오는 정보입니다
 */
interface BasicInfoRepository {

    companion object {
        val DEFAULT : BasicInfo = BasicInfo(180.0, 80.0, Gender.MAN, false)
    }

    /**
     * 건강정보를 불러오는 메소드 입니다. 각 구현체는 loadDefault가 true일경우 BasicInfoRepository.DEFAULT를 반환해야 합니다.
     * @param loadDefault 만약 저장된 건강정보가 없을경우 기본값을 불러올지 여부입니다. false시 저장된 값이 없을경우 예외가 발생됩니다.
     * @return 건강정보를 반환합니다.
     * @throws NoSuchElementException 저장된 건강정보가 없을경우, loadDefault값이 false로 지정되어 있을때 발생합니다.
     * @throws IllegalArgumentException 거의 발생하지 않지만, 저장된 정보가 손상되어 BasicInfo로 역직렬화 할 수 없을때 발생합니다.
     */
    suspend fun loadInfo(loadDefault : Boolean) : BasicInfo

    /**
     * 건강정보를 저장하는 메소드 입니다.
     * @param info 저장할 건강 정보입니다.
     */
    suspend fun saveInfo(info : BasicInfo)
}