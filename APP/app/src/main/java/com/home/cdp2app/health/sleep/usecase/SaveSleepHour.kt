package com.home.cdp2app.health.sleep.usecase

import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.repository.SleepRepository

/**
 * 수면시간 엔티티를 저장하는 유스케이스 입니다.
 * @see SleepRepository.writeSleepHours
 * @property sleepRepository 저장하는데 사용되는 레포지토리 입니다.
 */
class SaveSleepHour(private val sleepRepository: SleepRepository) {

    /**
     * 실제 수면시간을 저장하는 메소드 입니다.
     * @param data 저장하는 데이터 입니다.
     */
    suspend operator fun invoke(data : List<SleepHour>) {
        sleepRepository.writeSleepHours(data)
    }
}