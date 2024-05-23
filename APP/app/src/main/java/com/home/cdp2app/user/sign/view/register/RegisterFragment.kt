package com.home.cdp2app.user.sign.view.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.home.cdp2app.R
import com.home.cdp2app.databinding.AuthRegisterBinding
import com.home.cdp2app.common.network.type.NetworkStatus
import com.home.cdp2app.user.sign.repository.RemoteUserRepository
import com.home.cdp2app.user.sign.usecase.RegisterUseCase
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.home.cdp2app.user.sign.validator.RegisterValidator
import com.home.cdp2app.common.util.network.NetworkModule
import com.home.cdp2app.user.sign.view.callback.AuthCallback
import com.home.cdp2app.user.sign.view.register.validator.RegisterViewValidator
import com.home.cdp2app.user.sign.view.register.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private var callback : AuthCallback? = null
    //todo hilt inject
    private val validator : RegisterViewValidator = RegisterViewValidator(RegisterValidator())
    private val viewModel : RegisterViewModel by lazy {
        val repository = RemoteUserRepository(NetworkModule.userApi)
        RegisterViewModel(RegisterValidator(), RegisterUseCase(repository))
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
        super.onCreate(savedInstanceState)
        val binding = AuthRegisterBinding.inflate(inflater, container, false)
        initListener(binding)
        initObserver(binding)
        return binding.root
    }

    private fun initObserver(view : AuthRegisterBinding) {
        viewModel.validateLiveData.observe(viewLifecycleOwner) {
            it.getContent()?.let { status -> handleInvalidateStatus(status) } //vm validation 실패시 toast 요청
        }
        viewModel.registerStatusLiveData.observe(viewLifecycleOwner) {
            it.getContent()?.let { networkStatus -> handleNetworkStatus(networkStatus) }
            view.register.isEnabled = true //다시 회원가입 가능하게
            view.indicator.visibility = View.INVISIBLE //인디케이터 안보이게
        }
    }

    private fun initListener(view : AuthRegisterBinding) {
        // 체크할때만 회원가입 가능하게
        view.informApply.setOnCheckedChangeListener { _, isChecked ->
            view.register.isEnabled = isChecked
        }
        view.register.setOnClickListener {
            val status = validator.validate(view)
            if (status == ValidateStatus.OK) {
                viewModel.register(view.email.text.toString(), view.password.text.toString(), view.nickname.text.toString())
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
                Toast.makeText(requireContext(), R.string.signup_success, Toast.LENGTH_LONG).show()
                callback?.navigateSelect()
            }
            NetworkStatus.CONNECTION_ERROR -> Toast.makeText(requireContext(), R.string.connection_error, Toast.LENGTH_LONG).show()
            NetworkStatus.BAD_REQUEST -> Toast.makeText(requireContext(), R.string.connection_badrequest_signup, Toast.LENGTH_LONG).show() //이메일 중복
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