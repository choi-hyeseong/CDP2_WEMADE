package com.home.cdp2app.main.setting.basicinfo.usecase

import com.home.cdp2app.main.setting.basicinfo.entity.BasicInfo
import com.home.cdp2app.main.setting.basicinfo.repository.BasicInfoRepository

/**
 * BasicInfo를 로드하는 유스케이스.
 * @property basicInfoRepository BasicInfo를 로드할때 사용되는 레포지토리 입니다.
 */
class LoadBasicInfo(private val basicInfoRepository: BasicInfoRepository) {

    /**
     * 로드를 수행하는 메소드. invoke로 선언되어 바로 호출이 가능하다 (loadBasicInfo()) BasicInfoRepository.loadInfo를 수행한다.
     * @param loadDefault BasicInfo 로드 실패시 기본값을 불러올지 여부입니다.
     * @see BasicInfoRepository.loadInfo
     */
    suspend operator fun invoke(loadDefault : Boolean) : BasicInfo {
        return basicInfoRepository.loadInfo(loadDefault)
    }
}