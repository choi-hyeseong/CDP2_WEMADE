package com.home.cdp2app.tutorial.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.home.cdp2app.user.sign.view.AuthActivity
import com.home.cdp2app.databinding.TutorialBinding
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.startActivityWithBackstackClear
import com.home.cdp2app.tutorial.repository.PreferenceTutorialRepository
import com.home.cdp2app.tutorial.usecase.SaveTutorialCompleted
import com.home.cdp2app.tutorial.view.callback.TutorialCallback
import com.home.cdp2app.tutorial.view.fragment.TutorialFragment1
import com.home.cdp2app.tutorial.view.fragment.TutorialFragment2
import com.home.cdp2app.tutorial.view.fragment.TutorialFragment3
import com.home.cdp2app.tutorial.view.viewmodel.TutorialViewModel

class TutorialActivity : AppCompatActivity(), TutorialCallback {

    //todo hilt inject
    private val viewModel : TutorialViewModel by lazy {
        val storage = SharedPreferencesStorage(this)
        val repository = PreferenceTutorialRepository(storage)
        TutorialViewModel(SaveTutorialCompleted(repository))
    }
    // 튜토리얼
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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