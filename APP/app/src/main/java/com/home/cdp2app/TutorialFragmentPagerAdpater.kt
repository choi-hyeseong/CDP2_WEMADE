package com.home.cdp2app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TutorialFragmentPagerAdpater(activity: FragmentActivity): FragmentStateAdapter(activity) {
    // 튜토리얼 항목 추가 (ViewPager2에 항목을 추가)

    val fragments: List<Fragment>

    init {
        fragments = listOf(TutorialFragment1(), TutorialFragment2(), TutorialFragment3())
    }

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}