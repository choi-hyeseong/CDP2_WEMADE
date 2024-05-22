package com.home.cdp2app.user.auth.sign.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.cdp2app.main.view.MainPagerActivity
import com.home.cdp2app.R
import com.home.cdp2app.databinding.AuthBinding
import com.home.cdp2app.startActivityWithBackstackClear
import com.home.cdp2app.user.auth.sign.view.callback.AuthCallback
import com.home.cdp2app.user.auth.sign.view.login.LoginFragment
import com.home.cdp2app.user.auth.sign.view.register.RegisterFragment

//로그인 - 회원가입 관리하는 액티비티
class AuthActivity : AppCompatActivity(), AuthCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = AuthBinding.inflate(layoutInflater)
        navigateSelect()
        setContentView(bind.root)
    }

    override fun navigateSelect() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, AuthSelectFragment()).commit()
    }

    override fun navigateToRegister() {
        //backStack null로 넣으면 이름은 넣지 않지만, 이전 창을 backstack에 넣는다. - 뒤로가기 허용
        supportFragmentManager.beginTransaction().replace(R.id.frame, RegisterFragment()).addToBackStack(null).commit()
    }

    override fun navigateToLogin() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, LoginFragment()).addToBackStack(null).commit()
    }

    override fun navigateToMain() {
        //메인으로 이동
        startActivityWithBackstackClear(MainPagerActivity::class.java)
    }


}