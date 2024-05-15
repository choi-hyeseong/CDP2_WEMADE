package com.home.cdp2app.view.callback

/**
 * 회원가입 - 로그인 프래그먼트 콜백
 */
interface AuthCallback {

    /**
     * 회원가입 프래그먼트로 이동하기
     */
    fun navigateToRegister()

    /**
     * 로그인 프래그먼트로 이동하기
     */
    fun navigateToLogin()
}