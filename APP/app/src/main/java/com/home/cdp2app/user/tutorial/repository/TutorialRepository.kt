package com.home.cdp2app.user.tutorial.repository

/**
 * 튜토리얼 완료 여부를 관리하는 레포지토리
 */
interface TutorialRepository {

    /**
     * 튜토리얼 완료 여부를 확인하는 메소드
     * @return 튜토리얼 완료시 true, 아닐경우 false를 반환합니다
     */
    suspend fun checkCompleted() : Boolean

    /**
     * 튜토리얼을 완료했다고 저장하는 메소드
     */
    suspend fun setCompleted()
}