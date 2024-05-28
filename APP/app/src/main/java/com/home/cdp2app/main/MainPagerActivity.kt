package com.home.cdp2app.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.home.cdp2app.MainActivity
import com.home.cdp2app.R
import com.home.cdp2app.databinding.MainPagerBinding
import com.home.cdp2app.main.setting.basicinfo.repository.PreferenceBasicInfoRepository
import com.home.cdp2app.main.setting.basicinfo.usecase.HasBasicInfo
import com.home.cdp2app.main.setting.basicinfo.view.BasicInfoActivity
import com.home.cdp2app.main.dashboard.view.ChartDetailActivity
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.common.memory.SharedPreferencesStorage
import com.home.cdp2app.main.dashboard.view.callback.ChartDetailCallback
import com.home.cdp2app.main.dashboard.view.DashboardFragment
import com.home.cdp2app.main.predict.view.PredictFragment
import com.home.cdp2app.main.setting.SettingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPagerActivity : AppCompatActivity(), ChartDetailCallback {

    private val pagerViewModel : MainPagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = MainPagerBinding.inflate(layoutInflater)
        initTabLayout(bind)
        setContentView(bind.root)
        initObserver()
    }

    //basic info가 비어있을경우 설정화면으로 이동
    private fun initObserver() {
        pagerViewModel.checkHaveBasicInfo().observe(this) {
            it.getContent()?.let {hasInfo ->
                if (!hasInfo) {
                    Toast.makeText(this, R.string.insert_basic_info_firstjoin, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, BasicInfoActivity::class.java))
                }
            }
        }
    }

    private fun initTabLayout(view : MainPagerBinding) {
        view.pager.adapter = ViewpagerFragmentAdapter(this)
        TabLayoutMediator(view.tabLayout, view.pager) {tab, pos ->
            // icon & text setting
            when (pos) {
                0 -> {
                    //0번째 - 대시보드
                    tab.let {
                        it.text = getString(R.string.dashboard)
                        it.icon = AppCompatResources.getDrawable(this, R.drawable.dashboard_icon)
                    }
                }
                1 -> {
                    // icon by Freepik(flaticon)
                    //1번째 - 고혈압 평가 메뉴
                    tab.let {
                        it.text = getString(R.string.blood_pressure_predict)
                        it.icon = AppCompatResources.getDrawable(this, R.drawable.health_icon)
                    }
                }
                2 -> {
                    //2번째 - 설정 메뉴
                    tab.let {
                        it.text = getString(R.string.setting)
                        it.icon = AppCompatResources.getDrawable(this, R.drawable.setting_icon)
                    }
                }
            }
        }.attach()
    }

    class ViewpagerFragmentAdapter(mainPagerActivity: MainPagerActivity) : FragmentStateAdapter(mainPagerActivity) {

        private val fragments : List<Fragment> = listOf(DashboardFragment(), PredictFragment() , SettingFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }

    override fun navigateDetail(category: HealthCategory) {
        startActivity(Intent(this, ChartDetailActivity::class.java).putExtra(ChartDetailActivity.DETAIL_PARAM, category.toString()))
    }

    override fun navigateMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}