package com.home.cdp2app.user.sign.view.callback

/**
 * 회원가입 - 로그인 프래그먼트 콜백
 */
interface AuthCallback {

    /**
     * 선택 화면으로 이동 (회원가입 완료시)
     */
    fun navigateSelect()
    /**
     * 회원가입 프래그먼트로 이동하기
     */
    fun navigateToRegister()

    /**
     * 로그인 프래그먼트로 이동하기
     */
    fun navigateToLogin()

    /**
     * 메인 pager로 이동
     */
    fun navigateToMain()
}