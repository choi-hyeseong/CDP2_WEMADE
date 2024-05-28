package com.home.cdp2app.tutorial.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.cdp2app.databinding.TutorialThirdBinding
import com.home.cdp2app.tutorial.view.callback.TutorialCallback


class TutorialFragment3 : Fragment() {

    private var tutorialCallback : TutorialCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tutorialCallback = context as TutorialCallback?
    }

    override fun onDetach() {
        super.onDetach()
        tutorialCallback = null
    }

    // 튜토리얼 항목 3
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val bind = TutorialThirdBinding.inflate(inflater, container, false)
        bind.end.setOnClickListener {
            tutorialCallback?.onTutorialFinished()
            it.isClickable = false //중복 클릭 금지
        }
        return bind.root
    }
}