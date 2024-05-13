package com.home.cdp2app.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.cdp2app.BasicInfoActivity
import com.home.cdp2app.DashboardOrderActivity
import com.home.cdp2app.databinding.MainSettingBinding

class SettingFragment : Fragment() {

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
    }
}