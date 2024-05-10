package com.home.cdp2app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.PermissionController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.home.cdp2app.databinding.ActivityMainBinding
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.bloodpressure.repository.HealthConnectBloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.healthconnect.component.HealthConnectAPI
import com.home.cdp2app.health.healthconnect.component.HealthConnectStatus
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
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.view.fragment.DashboardFragment
import com.home.cdp2app.view.viewmodel.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.concurrent.ThreadLocalRandom

class MainActivity : AppCompatActivity() {

    private val LOG_HEADER = "MainActivity-Logger"
    private val permissions = HealthConnectAPI.PERMISSIONS
    private lateinit var requestPermission: ActivityResultLauncher<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        startActivity(Intent(this, MainPagerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK))) //다시 돌아가지 않음.
        /*


        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        handleHealthConnectSDK()
        initPermissionLauncher()

        CoroutineScope(Dispatchers.IO).launch {
            requestPermission()
        }

        //for test
        val dao = HealthConnectDao(applicationContext)
        val bloodPressureRepository = HealthConnectBloodPressureRepository(dao, BloodPressureMapper())
        CoroutineScope(Dispatchers.IO).launch {
            val bloodPressures = bloodPressureRepository.readBloodPressureBefore(Instant.now())
            //매 접속시 100~120 / 50~70사이의 혈압을 가진 데이터 기록
            bloodPressureRepository.writeBloodPressure(BloodPressure(Instant.now(), ThreadLocalRandom.current().nextDouble(100.0, 120.0), ThreadLocalRandom.current().nextDouble(50.0, 70.0)))
            //수축기/이완기 매핑
            val systolicMappedChart = BloodPressureSystolicChartMapper().convertToChart(bloodPressures)
            val diastolicMappedChart = BloodPressureDiastolicChartMapper().convertToChart(bloodPressures)
            //차트 매핑
            createChart(systolicMappedChart, bind.sleepChart)
            createChart(diastolicMappedChart, bind.chart)
        }/*
        수면시간, 심박수 차트 매핑 코드
        val heartRepository = HealthConnectHeartRepository(dao, HeartRateMapper())
        val mapper = HeartRateChartMapper()

        val sleepRepository = HealthConnectSleepRepository(SleepHourMapper(), dao)
        val sleepMapper = SleepHourChartMapper()
        CoroutineScope(Dispatchers.IO).launch {
            val heartRates = heartRepository.readHeartRateBefore(Instant.now())
            val sleepHours = sleepRepository.readSleepHoursBefore(Instant.now())
            heartRepository.writeHeartRate(
                listOf(
                    HeartRate(Instant.now(), ThreadLocalRandom.current().nextLong(150) + 1)
                ))
            sleepRepository.writeSleepHours(listOf(
                SleepHour(Instant.now(), Duration.ofMinutes(ThreadLocalRandom.current().nextLong(600) + 1))
            ))
            val mappedChart =
                mapper.convertToChart(heartRates) //RecyclerView가 사용하는 Chart data class로 변환
            val mappedSleep = sleepMapper.convertToChart(sleepHours)
            createChart(mappedChart, bind.chart)
            createChart(mappedSleep, bind.sleepChart)



        }
        */


    }

    suspend fun createChart(mappedChart: Chart, chart: BarChart) {
        withContext(Dispatchers.Main) {
            if (mappedChart.chartData.isEmpty()) //빈 데이터의경우 return
                return@withContext


            //instant를 float으로 변경하는게 아닌, Date를 따로 만들어서 index와 비교해서 보여주는 방식으로
            class Formatter(val date: List<Instant>) : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return SimpleDateFormat("MM-dd HH:mm", Locale.KOREA).format(Date.from(date[value.toInt()]))
                }
            }

            val entries = mutableListOf<BarEntry>() //entry에 빈값 들어가도 작동은 됨 안보일뿐
            chart.apply {
                //기본 bar chart 설정
                setDrawGridBackground(false)
                setDrawBarShadow(false)
                setDrawBorders(false)
                description = Description().apply { isEnabled = false }
                animateY(1000)
                animateX(1000)


                //xAxis 설정
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.textColor = R.color.black
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawGridLines(false)
                xAxis.labelCount = 7

                xAxis.valueFormatter = Formatter(mappedChart.chartData.map { it.time })

                //yAxis 설정
                axisLeft.setDrawAxisLine(false)
                axisLeft.textColor = R.color.black

                axisRight.isEnabled = false
                data = BarData(BarDataSet(entries, mappedChart.type.displayName))
                setVisibleXRangeMaximum(5f) //dateset 넣고 visible 설정가능
            }
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged() //data 갱신
            chart.invalidate() //view 갱신

        }
    }

    //sdk init
    private fun handleHealthConnectSDK() {
        val status: HealthConnectStatus = HealthConnectAPI.getSdkStatus(this)
        Log.i(LOG_HEADER, "Health Connect Status : $status")
        //이미 설치된 경우 핸들링 안함
        if (status == HealthConnectStatus.OK) return

        //지원하지 않는 기기 및 버젼일경우 메시지와 함께 종료
        if (status == HealthConnectStatus.NOT_SUPPORTED) {
            Toast.makeText(this, getString(R.string.health_not_supported), Toast.LENGTH_LONG).show()
            finish()
        }
        else {
            //설치가 필요한경우 ACTION_VIEW로 호출
            Toast.makeText(this, getString(R.string.health_require_install), Toast.LENGTH_LONG).show()
            startActivity(HealthConnectAPI.createInstallSdkIntent(this))
        }
    }

    //onCreate에서 Launcher 생성하기 위한 메소드
    private fun initPermissionLauncher() {
        //요청 contract와 callback
        requestPermission = registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
            //권한이 허용되지 않았을경우
            if (!granted.containsAll(permissions)) {
                Toast.makeText(this, R.string.health_permission_denied, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    //HealthConnect SDK Permission request
    private suspend fun requestPermission() {
        //요청될 permission
        val permissions = HealthConnectAPI.PERMISSIONS
        val healthConnectClient = HealthConnectAPI.getHealthConnectClient(applicationContext) //Singleton 준수하기 위한 Application Context 사용

        //이미 충분한 권한이 지급된경우 return
        if (healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)) return

        requestPermission.launch(permissions)
    }

         */
    }
}