package com.home.cdp2app.view.fragment.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.home.cdp2app.R
import com.home.cdp2app.databinding.AuthLoginBinding
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.rest.type.NetworkStatus
import com.home.cdp2app.user.auth.repository.PreferenceAuthTokenRepository
import com.home.cdp2app.user.auth.repository.RemoteUserRepository
import com.home.cdp2app.user.auth.usecase.LoginUseCase
import com.home.cdp2app.user.auth.usecase.SaveAuthToken
import com.home.cdp2app.valid.type.ValidateStatus
import com.home.cdp2app.user.auth.validator.LoginValidator
import com.home.cdp2app.util.network.NetworkModule
import com.home.cdp2app.view.callback.AuthCallback
import com.home.cdp2app.view.fragment.auth.validator.LoginViewValidator
import com.home.cdp2app.view.viewmodel.auth.LoginViewModel

class LoginFragment : Fragment() {
    // 로그인 화면
    private var callback : AuthCallback? = null
    // todo hilt inject
    private val validator : LoginViewValidator = LoginViewValidator(LoginValidator())
    private val viewModel : LoginViewModel by lazy {
        val authRepo = PreferenceAuthTokenRepository(SharedPreferencesStorage(requireContext()))
        val repository : RemoteUserRepository = RemoteUserRepository(NetworkModule.userApi)
        LoginViewModel(LoginValidator(), LoginUseCase(repository), SaveAuthToken(authRepo))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as AuthCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = AuthLoginBinding.inflate(layoutInflater, container, false)
        initListener(binding)
        initObserver(binding)
        return binding.root
    }

    private fun initObserver(view : AuthLoginBinding) {
        viewModel.validateLiveData.observe(viewLifecycleOwner) {
            it.getContent()?.let { status ->  handleInvalidateStatus(status) }
        }

        viewModel.loginStatusLiveData.observe(viewLifecycleOwner) {
            it.getContent()?.let { networkStatus -> handleNetworkStatus(networkStatus) }
            view.sync.isEnabled = true //다시 회원가입 가능하게
            view.indicator.visibility = View.INVISIBLE //인디케이터 안보이게
        }
    }

    private fun initListener(view : AuthLoginBinding) {
        // 체크할때만 회원가입 가능하게
        view.sync.setOnClickListener {
            val status = validator.validate(view)
            if (status == ValidateStatus.OK) {
                viewModel.login(view.email.text.toString(), view.password.text.toString())
                it.isEnabled = false //버튼 임시 비활성화
                view.indicator.visibility = View.VISIBLE //인디케이터 보이게
            }
            else
                handleInvalidateStatus(status) //OK가 아닌경우 에러 핸들링
        }
    }


    private fun handleNetworkStatus(status : NetworkStatus) {
        //네트워크 응답을 받았을때 핸들
        when (status) {
            NetworkStatus.OK -> {
                Toast.makeText(requireContext(), R.string.success_login, Toast.LENGTH_LONG).show()
                callback?.navigateToMain()
            }
            NetworkStatus.CONNECTION_ERROR -> Toast.makeText(requireContext(), R.string.connection_error, Toast.LENGTH_LONG).show()
            NetworkStatus.BAD_REQUEST -> Toast.makeText(requireContext(), R.string.signin_login_badrequest, Toast.LENGTH_LONG).show() //이메일 중복
            NetworkStatus.INTERNAL_ERROR -> Toast.makeText(requireContext(), R.string.internal_error, Toast.LENGTH_LONG).show()
            NetworkStatus.OTHER -> Toast.makeText(requireContext(), R.string.other_error, Toast.LENGTH_LONG).show()
            //여기서는 Auth를 사용하지 않으므로 핸들하지 않음
            NetworkStatus.UNAUTHORIZED -> {}
        }
    }

    private fun handleInvalidateStatus(result : ValidateStatus) {
        when (result) {
            ValidateStatus.FIELD_EMPTY -> Toast.makeText(requireContext(), getString(R.string.field_empty), Toast.LENGTH_LONG).show()
            ValidateStatus.VALUE_ERROR -> Toast.makeText(requireContext(), getString(R.string.value_error), Toast.LENGTH_LONG).show()
            else -> {} //핸들 안함
        }
    }
}