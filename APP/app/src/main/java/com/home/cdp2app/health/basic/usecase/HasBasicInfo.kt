package com.home.cdp2app.health.basic.usecase

import com.home.cdp2app.health.basic.repository.BasicInfoRepository

/**
 * BasicInfo가 저장되어 있는지 확인하는 유스케이스
 */
class HasBasicInfo(private val repository : BasicInfoRepository) {

    /**
     * 실제 저장여부를 확인하는 메소드
     * @return BasicInfo가 저장되어 있지 않아 기본값이 가져와질때 true를 반환합니다.
     */
    suspend operator fun invoke() : Boolean {
        return repository.hasInfo()
    }
}