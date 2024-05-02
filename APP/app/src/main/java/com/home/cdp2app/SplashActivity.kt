package com.home.cdp2app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.home.cdp2app.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    // 앱 실행 스플래시 화면
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 임의로 시작 시 튜토리얼 화면으로 연결, 자동 로그인 여부에 따라 달라질 필요 있음
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = (Intent(this, TutorialActivity::class.java))
            startActivity(intent)
            finish()
        }, 2000)
    }
}