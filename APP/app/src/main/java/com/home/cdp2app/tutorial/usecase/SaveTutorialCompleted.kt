package com.home.cdp2app.tutorial.usecase

import com.home.cdp2app.tutorial.repository.TutorialRepository

/**
 * 튜토리얼을 완료했다고 저장하는 유스케이스
 * @property tutorialRepository 저장하는데 사용하는 레포지토리
 */
class SaveTutorialCompleted(private val tutorialRepository: TutorialRepository) {

    /**
     * 튜토리얼 완료 여부를 저장하는 메소드
     * @see TutorialRepository.setCompleted
     */
    suspend operator fun invoke() {
        tutorialRepository.setCompleted()
    }
}