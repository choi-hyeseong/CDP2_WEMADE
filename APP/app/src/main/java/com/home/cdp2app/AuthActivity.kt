package com.home.cdp2app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.cdp2app.databinding.AuthBinding
import com.home.cdp2app.view.callback.AuthCallback
import com.home.cdp2app.view.fragment.auth.AuthSelectFragment
import com.home.cdp2app.view.fragment.auth.LoginFragment
import com.home.cdp2app.view.fragment.auth.RegisterFragment

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