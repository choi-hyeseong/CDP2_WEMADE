package com.home.cdp2app.health.bloodpressure.usecase

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository

/**
 * 혈압 엔티티를 저장하는 유스케이스 입니다.
 * @property BloodPressureRepository 혈압 엔티티 저장에 사용되는 레포지토리 입니다.
 * @see BloodPressureRepository.writeBloodPressure
 */
class SaveBloodPressure(private val bloodPressureRepository: BloodPressureRepository) {

    /**
     * 실제 저장이 수행되는 메소드 입니다.
     * @param bloodPressure 혈압 엔티티 입니다.
     */
    suspend operator fun invoke(bloodPressure: BloodPressure) {
        bloodPressureRepository.writeBloodPressure(bloodPressure)
    }
}