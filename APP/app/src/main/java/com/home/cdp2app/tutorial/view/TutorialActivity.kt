package com.home.cdp2app.tutorial.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.home.cdp2app.databinding.TutorialBinding
import com.home.cdp2app.startActivityWithBackstackClear
import com.home.cdp2app.tutorial.view.callback.TutorialCallback
import com.home.cdp2app.tutorial.view.fragment.TutorialFragment1
import com.home.cdp2app.tutorial.view.fragment.TutorialFragment2
import com.home.cdp2app.tutorial.view.fragment.TutorialFragment3
import com.home.cdp2app.tutorial.view.viewmodel.TutorialViewModel
import com.home.cdp2app.user.sign.view.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialActivity : AppCompatActivity(), TutorialCallback {

    private val viewModel : TutorialViewModel by viewModels()

    // 튜토리얼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val binding = TutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Viewpager 부착 및 Indicator 연결
        binding.viewpager.adapter = TutorialFragmentPagerAdapter(this)
        binding.viewpagerIndicator.attachTo(binding.viewpager)

    }

    override fun onTutorialFinished() {
        viewModel.saveTutorialEnded().observe(this) {
            //저장은 항상 성공하므로 auth로 이동
            startActivityWithBackstackClear(AuthActivity::class.java)
        }
    }

    class TutorialFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        // 튜토리얼 항목 추가 (ViewPager2에 항목을 추가)

        private val fragments: List<Fragment> = listOf(TutorialFragment1(), TutorialFragment2(), TutorialFragment3())

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }


}