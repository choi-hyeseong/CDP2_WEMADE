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

class MainActivity : AppCompatActivity(), HealthConnectSuccessCallback {

    private val LOG_HEADER = "MainActivity-Logger"
    private val permissions = HealthConnectAPI.PERMISSIONS
    private lateinit var requestPermission: ActivityResultLauncher<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        handleHealthConnectSDK()
        initPermissionLauncher(this) //펄미션 요청이 끝난 후 검증용 콜백

        CoroutineScope(Dispatchers.IO).launch {
            requestPermission(this@MainActivity)
        }
        //


        /*
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)


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
        }
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
    private fun initPermissionLauncher(callback : HealthConnectSuccessCallback) {
        //요청 contract와 callback
        requestPermission = registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
            //권한이 허용되지 않았을경우
            if (!granted.containsAll(permissions)) {
                Toast.makeText(this, R.string.health_permission_denied, Toast.LENGTH_SHORT).show()
                finish()
            }
            else
                callback.onSuccess()
        }
    }

    //HealthConnect SDK Permission request
    private suspend fun requestPermission(callback: HealthConnectSuccessCallback) {
        //요청될 permission
        val permissions = HealthConnectAPI.PERMISSIONS
        val healthConnectClient = HealthConnectAPI.getHealthConnectClient(applicationContext) //Singleton 준수하기 위한 Application Context 사용

        //이미 충분한 권한이 지급된경우 정상 작동하므로 callback 실행 후 return
        if (healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)) {
            callback.onSuccess()
            return
        }

        requestPermission.launch(permissions)
    }

    override fun onSuccess() {
        startActivity(Intent(this, MainPagerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK))) //다시 돌아가지 않음.
    }

}

// helath connect 설정이 모두 완료됐을때 수행하는 callback
interface HealthConnectSuccessCallback {
    fun onSuccess()
}