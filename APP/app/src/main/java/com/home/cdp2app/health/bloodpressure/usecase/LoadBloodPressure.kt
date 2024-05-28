package com.home.cdp2app.health.bloodpressure.usecase

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import java.time.Instant

/**
 * 혈압 데이터 가져오는 유스케이스
 * @param bloodPressureRepository 혈압을 가져올 레포지토리 입니다.
 */
class LoadBloodPressure(private val bloodPressureRepository: BloodPressureRepository) {

    /**
     * 혈압 데이터를 가져오는 메소드
     * @param date 가져올 기준 시점입니다.
     * @return BloodPressure 엔티티의 list를 반환합니다. 비어있을 수 있습니다.
     * @see BloodPressureRepository.readBloodPressureBefore
     */
    suspend operator fun invoke(date : Instant) : List<BloodPressure> {
        return bloodPressureRepository.readBloodPressureBefore(date)
    }
}