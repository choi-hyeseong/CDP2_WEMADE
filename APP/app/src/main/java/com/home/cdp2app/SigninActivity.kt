package com.home.cdp2app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.internal.TextWatcherAdapter
import com.home.cdp2app.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    // 로그인 화면
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 텍스트 지정
        binding.textinputId.title.setText(R.string.account_id)
        binding.textinputId.content.setHint(R.string.account_id_hint)
        binding.textinputPw.title.setText(R.string.account_pw)
        binding.textinputPw.content.setHint(R.string.account_pw_hint)
        binding.btnSignin.root.setText(R.string.signin)

        // 최대 입력 글자 수 제한
        binding.textinputId.content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        binding.textinputPw.content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))

        // 비밀번호 가리기
        binding.textinputPw.content.transformationMethod = PasswordTransformationMethod.getInstance()
        
        // 리스너 지정
        binding.textviewForgotPw.setOnClickListener {
            // 비밀번호 찾기 화면으로 연결 예정, 구현 논의 필요
            Toast.makeText(this, "그렇군요!", Toast.LENGTH_LONG).show()
        }
        binding.btnSignin.root.setOnClickListener {
            // id 조건 확인
            val id = binding.textinputId.content.text.toString()
            val idRegexFilter = Regex("[a-zA-Z0-9]{6,20}")
            if (!idRegexFilter.containsMatchIn(id)) {
                Toast.makeText(this, R.string.account_id_warning, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 비밀번호 조건 확인
            val pw = binding.textinputPw.content.text.toString()
            val pwRegexFilter = Regex("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*?_]).{8,20}")
            if (!pwRegexFilter.containsMatchIn(pw)) {
                Toast.makeText(this, R.string.account_pw_warning, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 메인 화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        }
    }
}