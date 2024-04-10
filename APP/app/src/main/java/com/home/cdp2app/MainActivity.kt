package com.home.cdp2app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.home.cdp2app.databinding.ActivityMainBinding
import com.home.cdp2app.health.healthconnect.component.HealthConnectAPI
import com.home.cdp2app.health.healthconnect.component.HealthConnectStatus
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.heart.repository.HealthConnectHeartRepository
import com.home.cdp2app.view.chart.mapper.HeartRateChartMapper
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
            heartRepository.writeHeartRate(
                listOf(
                    HeartRate(Instant.now(), ThreadLocalRandom.current().nextLong(150))
                ))
            val mappedChart =
                mapper.convertToChart(heartRates) //RecyclerView가 사용하는 Chart data class로 변환 (현재는 내부 아이템인 ChartItem만 사용)
            withContext(Dispatchers.Main) {

                val chart = bind.chart

                //instant를 float으로 변경하는게 아닌, Date를 따로 만들어서 index와 비교해서 보여주는 방식으로
                class Formatter(val date: List<Instant>) : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return SimpleDateFormat("MM-dd HH:mm", Locale.KOREA).format(Date.from(date[value.toInt()]))
                    }
                }

                val entries = mappedChart.chartData.mapIndexed { index, it -> BarEntry(index.toFloat(), it.data.toFloat()) }
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
                    data = BarData(BarDataSet(entries, "HeartRate"))
                    setVisibleXRangeMaximum(5f) //dateset 넣고 visible 설정가능
                }
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged() //data 갱신
                chart.invalidate() //view 갱신

            }


        }

    }

    //sdk init
    private fun handleHealthConnectSDK() {
        val status: HealthConnectStatus = HealthConnectAPI.getSdkStatus(this)
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
            Toast.makeText(this, getString(R.string.health_require_install), Toast.LENGTH_LONG)
                .show()
            startActivity(HealthConnectAPI.createInstallSdkIntent(this))
        }
    }

    //onCreate에서 Launcher 생성하기 위한 메소드
    private fun initPermissionLauncher() {
        //요청 contract와 callback
        requestPermission =
            registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
                //권한이 허용되지 않았을경우
                if (!granted.containsAll(permissions)) {
                    Toast.makeText(this, R.string.health_permission_denied, Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
    }

    //HealthConnect SDK Permission request
    private suspend fun requestPermission() {
        //요청될 permission
        val permissions = HealthConnectAPI.PERMISSIONS
        val healthConnectClient =
            HealthConnectAPI.getHealthConnectClient(applicationContext) //Singleton 준수하기 위한 Application Context 사용

        //이미 충분한 권한이 지급된경우 return
        if (healthConnectClient.permissionController.getGrantedPermissions()
                .containsAll(permissions))
            return

        requestPermission.launch(permissions)
    }
}