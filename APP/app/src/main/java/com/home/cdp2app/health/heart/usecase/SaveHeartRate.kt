package com.home.cdp2app.health.heart.usecase

import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.repository.HeartRepository

/**
 * HeartRate Entity를 저장하는 유스케이스 입니다.
 * @see HeartRepository.writeHeartRate
 * @property heartRepository 저장하는데 사용되는 레포지토리 입니다.
 */
class SaveHeartRate(private val heartRepository: HeartRepository) {

    /**
     * 실제 저장하는 메소드 입니다.
     * @param heartList 저장할 엔티티 입니다.
     */
    suspend operator fun invoke(heartList : List<HeartRate>) {
        heartRepository.writeHeartRate(heartList)
    }
}