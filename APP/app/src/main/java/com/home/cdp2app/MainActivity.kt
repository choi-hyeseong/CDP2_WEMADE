package com.home.cdp2app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.home.cdp2app.databinding.ActivityMainBinding
import com.home.cdp2app.health.healthconnect.component.HealthConnectAPI
import com.home.cdp2app.health.healthconnect.component.HealthConnectStatus
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.heart.repository.HealthConnectHeartRepository
import com.home.cdp2app.view.chart.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.toEntry
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
    private lateinit var requestPermission : ActivityResultLauncher<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        handleHealthConnectSDK()
        initPermissionLauncher()

        CoroutineScope(Dispatchers.IO).launch {
            requestPermission()
        }

        //for test
        val dao = HealthConnectDao(applicationContext)
        val heartRepository = HealthConnectHeartRepository(dao, HeartRateMapper())
        val mapper = HeartRateChartMapper()
        CoroutineScope(Dispatchers.IO).launch {
            val heartRates = heartRepository.readHeartRateBefore(Instant.now())
            heartRates.forEach {
                Log.i(LOG_HEADER, "Read Record - Time : ${it.time}, bpm : ${it.bpm}")
            }
            heartRepository.writeHeartRate(listOf(
                HeartRate(Instant.now(), ThreadLocalRandom.current().nextLong(150))
            ))
            withContext(Dispatchers.Main) {
                val charData = mapper.convertToChart(heartRates) //RecyclerView가 사용하는 Chart data class로 변환 (현재는 내부 아이템인 ChartItem만 사용)
                val chart = bind.chart

                val entries = charData.toEntry()
                //하나의 line을 구성하는 LineDataSet 구성. 이때, Chart에서 사용하는 Entry(Float, Float)으로 변환.
                //toEntry는 Chart.kt에 명시되어 ChartItem(Instant, Double)을 Entry로 변환함.
                val lineDataSet = LineDataSet(entries, "HeartRate").apply {
                    lineWidth = 2f
                    circleRadius = 1f
                    color = resources.getColor(R.color.black)
                }
                chart.data = LineData(lineDataSet) //dataset을 linedata에 넣어 chart에 주입
                chart.apply {
                    isAutoScaleMinMaxEnabled = true
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.textColor = R.color.black
                    xAxis.setLabelCount(100, true)
                    xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                        //float형식의 epoch second를 date로 변경
                        override fun getFormattedValue(value: Float): String {
                            val format = SimpleDateFormat("HH:mm:ss", Locale.KOREA).format(Date.from(Instant.ofEpochSecond((value).toLong())))
                            return format
                        }
                    }
                }
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged() //data 갱신
                chart.invalidate() //view 갱신
            }



        }

    }

    //sdk init
    private fun handleHealthConnectSDK() {
        val status : HealthConnectStatus = HealthConnectAPI.getSdkStatus(this)
        Log.i(LOG_HEADER, "Health Connect Status : $status")
        //이미 설치된 경우 핸들링 안함
        if (status == HealthConnectStatus.OK)
            return

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
        if (healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions))
            return

        requestPermission.launch(permissions)
    }
}