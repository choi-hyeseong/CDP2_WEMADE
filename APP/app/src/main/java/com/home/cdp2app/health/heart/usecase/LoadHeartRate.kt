package com.home.cdp2app.health.heart.usecase

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.repository.HeartRepository
import java.time.Instant

/**
 * 심박수를 가져오는 유스케이스
 * @param heartRepository 심박수를 가져올 레포지토리 입니다.
 */
class LoadHeartRate(private val heartRepository: HeartRepository) {

    /**
     * 심박수를 가져오는 메소드
     * @param date 가져올때 기준이 되는 시간입니다.
     * @return HeartRate의 List를 반환합니다.
     * @see HeartRepository.readHeartRateBefore
     */
    suspend operator fun invoke(date : Instant) : List<HeartRate> {
        return heartRepository.readHeartRateBefore(date)
    }
}