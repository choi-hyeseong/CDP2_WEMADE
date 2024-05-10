package com.home.cdp2app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.home.cdp2app.databinding.MainPagerBinding
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.bloodpressure.repository.HealthConnectBloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.heart.repository.HealthConnectHeartRepository
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.order.repository.PreferenceOrderRepository
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.health.order.usecase.LoadChartOrder
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import com.home.cdp2app.health.sleep.repository.HealthConnectSleepRepository
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.view.chart.ChartCallback
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.view.fragment.DashboardFragment
import com.home.cdp2app.view.fragment.MainFragment
import com.home.cdp2app.view.fragment.SettingFragment
import com.home.cdp2app.view.viewmodel.DashboardViewModel

class MainPagerActivity : AppCompatActivity(), ChartCallback {

    // todo hilt inject
    private val dashboardViewModel : DashboardViewModel by lazy {
        val repository = PreferenceOrderRepository(SharedPreferencesStorage(applicationContext))
        val healthDao = HealthConnectDao(applicationContext)
        val bloodRepo = HealthConnectBloodPressureRepository(healthDao, BloodPressureMapper())
        val heartRepo = HealthConnectHeartRepository(healthDao, HeartRateMapper())
        val sleepRepo = HealthConnectSleepRepository(SleepHourMapper(), healthDao)
       DashboardViewModel(
           LoadChartOrder(repository),
            LoadHeartRate(heartRepo),
            LoadBloodPressure(bloodRepo),
            LoadSleepHour(sleepRepo),
            ChartParser(listOf(HeartRateChartMapper(), BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper(), SleepHourChartMapper()))
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = MainPagerBinding.inflate(layoutInflater)
        bind.pager.adapter = ViewpagerFragmentAdapter(this, dashboardViewModel)
        TabLayoutMediator(bind.tabLayout, bind.pager) {tab, pos -> tab.text = "$pos 번"}.attach()
        setContentView(bind.root)
    }

    class ViewpagerFragmentAdapter(mainPagerActivity: MainPagerActivity, dashboardViewModel: DashboardViewModel) : FragmentStateAdapter(mainPagerActivity) {

        private val fragments : List<Fragment> = listOf(MainFragment(), DashboardFragment(dashboardViewModel), SettingFragment())

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
}