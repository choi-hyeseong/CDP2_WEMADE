package com.home.cdp2app.health.bloodpressure.repository

import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import java.time.Instant

/**
 * 혈압 데이터를 관리 및 접근하는 레포지토리
 */
interface BloodPressureRepository {

    /**
     * 특정 날짜 이전의 혈압을 읽는 메소드
     * @param date 기준 날짜입니다.
     * @return BloodPressure 리스트 엔티티입니다.
     */
    suspend fun readBloodPressureBefore(date: Instant): List<BloodPressure>

    /**
     * 혈압을 기록하는 메소드
     * @param pressure 혈압 엔티티입니다. 날짜가 포함되어 있습니다.
     */
    suspend fun writeBloodPressure(pressure: BloodPressure)
}