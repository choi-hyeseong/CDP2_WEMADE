package com.home.cdp2app.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.cdp2app.BasicInfoActivity
import com.home.cdp2app.DashboardOrderActivity
import com.home.cdp2app.databinding.MainSettingBinding
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.user.auth.repository.PreferenceAuthRepository
import com.home.cdp2app.user.auth.usecase.DeleteAuthToken
import com.home.cdp2app.view.callback.MainPagerCallback
import com.home.cdp2app.view.viewmodel.setting.SettingViewModel

class SettingFragment : Fragment() {

    private var callback : MainPagerCallback? = null

    //todo hilt inject
    private val viewModel : SettingViewModel by lazy {
        val storage = SharedPreferencesStorage(requireContext())
        SettingViewModel(DeleteAuthToken(PreferenceAuthRepository(storage)))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as MainPagerCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = MainSettingBinding.inflate(inflater, container, false)
        initListener(bind)
        return bind.root
    }

    // setup click listener
    private fun initListener(view : MainSettingBinding) {
        view.userInfo.setOnClickListener {
            startActivity(Intent(requireActivity(), BasicInfoActivity::class.java))
        }
        view.changeOrder.setOnClickListener {
            startActivity(Intent(requireActivity(), DashboardOrderActivity::class.java))
        }
        //로그아웃
        view.signOut.setOnClickListener {
            signOut(it) //버튼 상태 변경위한 메소드
        }
    }

    private fun signOut(view : View) {
        view.isClickable = false //두번이상 클릭 못하게 막기
        viewModel.signOut().observe(this) { event ->
            //이벤트 핸들 안될경우 리턴
            val isSuccess = event.getContent() ?: return@observe
            if (isSuccess) {
                callback?.navigateMain()
                view.isClickable = true //혹시나 navigate 미작동 대비해서 click 허용
            }
        }
    }
}