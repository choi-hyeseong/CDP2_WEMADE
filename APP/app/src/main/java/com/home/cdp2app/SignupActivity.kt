package com.home.cdp2app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.home.cdp2app.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 텍스트 지정
        binding.textinputId.title.setText(R.string.account_id)
        binding.textinputId.content.setHint(R.string.account_id_hint)
        binding.textinputPw.title.setText(R.string.account_pw)
        binding.textinputPw.content.setHint(R.string.account_pw_hint)
        binding.textinputPwAgain.title.setText(R.string.account_pw_again)
        binding.textinputPwAgain.content.setHint(R.string.account_pw_again_hint)
        binding.btnSignup.root.setText(R.string.signup)

        // 최대 입력 글자 수 제한
        binding.textinputId.content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        binding.textinputPw.content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        binding.textinputPwAgain.content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))

        // 비밀번호 가리기
        binding.textinputPw.content.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.textinputPwAgain.content.transformationMethod = PasswordTransformationMethod.getInstance()

        // 리스너 지정
        binding.btnSignup.root.setOnClickListener {
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

            // 비밀번호 일치 확인
            val pwAgain = binding.textinputPwAgain.content.text.toString()
            if (!pwAgain.equals(pw)) {
                Toast.makeText(this, R.string.account_pw_again_warning, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 회원가입 후에는 초기 정보 입력 화면으로 이동
            val intent = Intent(this, PersonalInformationActivity::class.java)
            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        }
    }
}