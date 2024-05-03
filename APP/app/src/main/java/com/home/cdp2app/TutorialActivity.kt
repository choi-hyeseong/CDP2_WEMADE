package com.home.cdp2app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.home.cdp2app.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {
    // 튜토리얼, 로그인과 회원 가입 선택 화면
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 및 로그인 성공 시, 튜토리얼 액티비티를 종료하기 위함
        var signupResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK)
                finish()
        }

        // Viewpager 부착 및 Indicator 연결
        binding.viewpager.adapter = TutorialFragmentPagerAdpater(this)
        binding.viewpagerIndicator.attachTo(binding.viewpager)

        // 텍스트 지정
        binding.btnSignup.root.setText(R.string.signup)
        binding.btnSignin.root.setText(R.string.signin)

        // 리스너 지정
        binding.btnSignin.root.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            signupResult.launch(intent)
        }
        binding.btnSignup.root.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}