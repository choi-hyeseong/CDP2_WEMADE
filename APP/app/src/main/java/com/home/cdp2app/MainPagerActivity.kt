package com.home.cdp2app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.home.cdp2app.databinding.MainPagerBinding
import com.home.cdp2app.health.basic.repository.PreferenceBasicInfoRepository
import com.home.cdp2app.health.basic.usecase.HasBasicInfo
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
import com.home.cdp2app.view.callback.MainPagerCallback
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.view.fragment.DashboardFragment
import com.home.cdp2app.view.fragment.PredictFragment
import com.home.cdp2app.view.fragment.SettingFragment
import com.home.cdp2app.view.viewmodel.MainPagerViewModel
import com.home.cdp2app.view.viewmodel.dashboard.DashboardViewModel

class MainPagerActivity : AppCompatActivity(), MainPagerCallback {

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

    private val pagerViewModel : MainPagerViewModel by lazy {
        val storage = SharedPreferencesStorage(this)
        val repository = PreferenceBasicInfoRepository(storage)
        MainPagerViewModel(HasBasicInfo(repository))
    }

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
        view.pager.adapter = ViewpagerFragmentAdapter(this, dashboardViewModel)
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

    class ViewpagerFragmentAdapter(mainPagerActivity: MainPagerActivity, dashboardViewModel: DashboardViewModel) : FragmentStateAdapter(mainPagerActivity) {

        private val fragments : List<Fragment> = listOf(DashboardFragment(dashboardViewModel), PredictFragment() , SettingFragment())

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