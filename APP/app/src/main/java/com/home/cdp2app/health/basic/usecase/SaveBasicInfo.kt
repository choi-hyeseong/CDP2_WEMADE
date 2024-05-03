package com.home.cdp2app.health.basic.usecase

import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.health.basic.repository.BasicInfoRepository

/**
 * BasicInfo를 저장하는 유스케이스 입니다.
 * @property basicInfoRepository BasicInfo를 저장할 레포지토리 입니다.
 */
class SaveBasicInfo(private val basicInfoRepository: BasicInfoRepository) {

    /**
     * BasicInfo의 저장을 수행하는 메소드 입니다.
     * @param info 저장할 BasicInfo 입니다.
     * @see BasicInfoRepository.saveInfo
     */
    suspend operator fun invoke(info : BasicInfo) {
        basicInfoRepository.saveInfo(info)
    }
}