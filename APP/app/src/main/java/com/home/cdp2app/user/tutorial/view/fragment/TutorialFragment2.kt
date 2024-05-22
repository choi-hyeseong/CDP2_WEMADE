package com.home.cdp2app.user.tutorial.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.cdp2app.databinding.TutorialSecondBinding

class TutorialFragment2 : Fragment() {
    // 튜토리얼 항목 2
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return TutorialSecondBinding.inflate(inflater, container, false).root
    }
}