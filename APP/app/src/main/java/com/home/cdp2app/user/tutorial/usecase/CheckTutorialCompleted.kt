package com.home.cdp2app.user.tutorial.usecase

import com.home.cdp2app.user.tutorial.repository.TutorialRepository

/**
 * 튜토리얼을 완료했는지 여부를 확인하는 유스케이스
 * @see TutorialRepository.checkCompleted
 */
class CheckTutorialCompleted(private val tutorialRepository: TutorialRepository) {

    /**
     * 튜토리얼을 완료했는지 반환하는 메소드
     * @return 튜토리얼을 완료한경우 true, 아닌경우 false를 반환합니다.
     */
    suspend operator fun invoke() : Boolean {
        return tutorialRepository.checkCompleted()
    }
}