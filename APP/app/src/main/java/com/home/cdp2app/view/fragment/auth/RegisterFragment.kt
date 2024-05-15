package com.home.cdp2app.view.fragment.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.home.cdp2app.R
import com.home.cdp2app.databinding.AuthRegisterBinding
import com.home.cdp2app.type.ValidateStatus
import com.home.cdp2app.user.auth.validator.RegisterValidator
import com.home.cdp2app.view.fragment.auth.validator.RegisterViewValidator

class RegisterFragment : Fragment() {

    //todo hilt inject
    private val validator : RegisterViewValidator = RegisterViewValidator(RegisterValidator())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val binding = AuthRegisterBinding.inflate(inflater, container, false)
        initListener(binding)
        return binding.root
    }

    private fun initListener(view : AuthRegisterBinding) {
        // 체크할때만 회원가입 가능하게
        view.informApply.setOnCheckedChangeListener { _, isChecked ->
            view.register.isEnabled = isChecked
        }
        view.register.setOnClickListener {
            handleValidateResult(validator.validate(view))
        }
    }

    private fun handleValidateResult(result : ValidateStatus) {
        when (result) {
            ValidateStatus.OK -> Toast.makeText(requireContext(), "성공", Toast.LENGTH_LONG).show()
            ValidateStatus.FIELD_EMPTY -> Toast.makeText(requireContext(), getString(R.string.field_empty), Toast.LENGTH_LONG).show()
            ValidateStatus.VALUE_ERROR -> Toast.makeText(requireContext(), getString(R.string.value_error), Toast.LENGTH_LONG).show()
        }
    }
}