package com.home.cdp2app.main.dashboard.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.common.util.livedata.Event
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.main.dashboard.chart.Chart
import com.home.cdp2app.main.dashboard.chart.applyChart
import com.home.cdp2app.main.dashboard.chart.parser.ChartParser
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.setting.order.usecase.LoadChartOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/**
 * 대쉬보드 관리에 사용되는 VM
 * @property loadChartOrder RecyclerView에서 차트 순서를 불러오는데 사용됩니다.
 * @property loadHeartRate 심박수를 가져올때 사용됩니다.
 * @property loadBloodPressure 혈압(수축기, 이완기)를 가져올때 사용됩니다.
 * @property loadSleepHour 수면시간을 가져올때 사용됩니다.
 * @property chartParser 읽어온 엔티티를 차트로 파싱할때 사용됩니다. 사용하는 매퍼는 초기화 되어 있습니다.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(private val loadChartOrder: LoadChartOrder,
                                             private val loadHeartRate: LoadHeartRate,
                                             private val loadBloodPressure: LoadBloodPressure,
                                             private val loadSleepHour: LoadSleepHour,
                                             private val chartParser: ChartParser) : ViewModel() {

    private val LOG_HEADER : String = "Fragment_Dashboard" //for log

    //실제 vm에서 관리할 차트 데이터
    private lateinit var chartList : MutableList<Chart>
    val toastLiveData : MutableLiveData<Event<HealthCategory>> = MutableLiveData() //특정 sync toast 알림 위한 이벤트 라이브데이터
    // 기존 init보다 lazy가 더 나아 order - chart 의 flow를 하나로 합쳐서 해결
    val chartLiveData: MutableLiveData<MutableList<Chart>> by lazy {
        MutableLiveData<MutableList<Chart>>().also { loadAllChartData() }
    }

    //최초로 한번 모든 차트 데이터를 불러오는 기능
    private fun loadAllChartData() {
        CoroutineScope(Dispatchers.IO).launch {
            chartList = loadChartOrder().toEmptyChart() //order load
            val date = Instant.now()
            val heartRateJob = async { loadHeartRateChart(date) }
            val sleepHourJob = async { loadSleepHourChart(date) }
            val systolicJob = async { loadBloodPressureSystolicChart(date) }
            val diastolicJob = async { loadBloodPressureDiastolicChart(date) }
            // async - await를 이용해서 동시에 로드할 수 있도록 수행
            heartRateJob.await()
            sleepHourJob.await()
            systolicJob.await()
            diastolicJob.await()
            chartLiveData.postValue(chartList) //위 job이 업데이트 하지 못할것 대비 (test)
        }

    }

    //동기화 버튼 클릭
    fun requestSync(category: HealthCategory) {
        val date : Instant = Instant.now()
        CoroutineScope(Dispatchers.IO).launch {
            when (category) {
                HealthCategory.HEART_RATE -> loadHeartRateChart(date)
                HealthCategory.BLOOD_PRESSURE_SYSTOLIC -> loadBloodPressureSystolicChart(date)
                HealthCategory.BLOOD_PRESSURE_DIASTOLIC -> loadBloodPressureDiastolicChart(date)
                HealthCategory.SLEEP_HOUR -> loadSleepHourChart(date)
            }
            toastLiveData.postValue(Event(category))
        }
    }

    suspend fun loadHeartRateChart(date : Instant) {
        val result = loadHeartRate(date)
        if (result.isEmpty())
            Log.w(LOG_HEADER, "심박수 데이터가 비어있습니다.")
        else
            updateChart(result, HealthCategory.HEART_RATE)
    }

    suspend fun loadSleepHourChart(date : Instant) {
        val result = loadSleepHour(date)
        if (result.isEmpty())
            Log.w(LOG_HEADER, "수면시간 데이터가 비어있습니다.")
        else
            updateChart(result, HealthCategory.SLEEP_HOUR)
    }

    suspend fun loadBloodPressureDiastolicChart(date : Instant) {
        val result = loadBloodPressure(date)
        if (result.isEmpty())
            Log.w(LOG_HEADER, "혈압 데이터가 비어있습니다.")
        else
            updateChart(result, HealthCategory.BLOOD_PRESSURE_DIASTOLIC)
    }

    suspend fun loadBloodPressureSystolicChart(date : Instant) {
        val result = loadBloodPressure(date)
        if (result.isEmpty())
            Log.w(LOG_HEADER, "혈압 데이터가 비어있습니다.")
        else
            updateChart(result, HealthCategory.BLOOD_PRESSURE_SYSTOLIC)
    }

    //읽어온 데이터를 이용해서 차트로 파싱하는 메소드
    //data는 비어있어선 안됨.
    private fun updateChart(data : List<*>, category : HealthCategory) {
        //차트가 초기화 되어 있지 않을경우 ignore
        //현재 변수로 따로 빼놓은 상태에서 전처럼 livedata로 접근하면 아직 초기화되지 않은 value라 계속 업데이트가 안됐음. 변수로 변경후 해결
        if (chartList.isEmpty()) {
            Log.w(LOG_HEADER, "can't update recycler view. chart is empty.")
            return
        }

        val parsedChart = chartParser.parse(data, category) //파싱
        kotlin.runCatching {
            chartList.applyChart(parsedChart)
        }.onFailure {
            Log.w(LOG_HEADER, "can't update recycler view. can't find chart index")
        }.onSuccess {
            chartLiveData.postValue(chartList) //notify
        }
    }

}