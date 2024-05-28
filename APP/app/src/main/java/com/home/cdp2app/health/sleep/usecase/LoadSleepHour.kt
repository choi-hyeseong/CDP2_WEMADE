package com.home.cdp2app.health.sleep.usecase

import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.repository.SleepRepository
import java.time.Instant

/**
 * 수면시간을 불러오는 유스케이스
 * @param sleepRepository 수면시간을 가져올 레포지토리 입니다.
 */
class LoadSleepHour(private val sleepRepository: SleepRepository) {

    /**
     * 수면시간을 읽는 메소드 입니다.
     * @param date 읽을때 기준이 되는 시간입니다.
     * @return SleepHour의 리스트입니다. 비어있을 수 있습니다.
     * @see SleepRepository.readSleepHoursBefore()
     */
    suspend operator fun invoke(date : Instant) : List<SleepHour> {
       return sleepRepository.readSleepHoursBefore(date)
    }
}