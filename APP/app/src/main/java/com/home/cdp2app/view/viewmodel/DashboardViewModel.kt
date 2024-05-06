package com.home.cdp2app.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.health.order.usecase.LoadChartOrder
import com.home.cdp2app.health.order.usecase.SaveChartOrder
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.util.livedata.Event
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.parser.ChartParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Instant

/**
 * 대쉬보드 관리에 사용되는 VM
 * @property loadChartOrder RecyclerView에서 차트 순서를 불러오는데 사용됩니다.
 * @property loadHeartRate 심박수를 가져올때 사용됩니다.
 * @property loadBloodPressure 혈압(수축기, 이완기)를 가져올때 사용됩니다.
 * @property loadSleepHour 수면시간을 가져올때 사용됩니다.
 * @property chartParser 읽어온 엔티티를 차트로 파싱할때 사용됩니다. 사용하는 매퍼는 초기화 되어 있습니다.
 */
class DashboardViewModel(private val loadChartOrder: LoadChartOrder,
                         private val loadHeartRate: LoadHeartRate,
                         private val loadBloodPressure: LoadBloodPressure,
                         private val loadSleepHour: LoadSleepHour,
                         private val chartParser: ChartParser) {

    private val LOG_HEADER : String = "Fragment_Dashboard" //for log

    val toastLiveData : MutableLiveData<Event<HealthCategory>> = MutableLiveData() //특정 sync toast 알림 위한 이벤트 라이브데이터
    //lazy로 선언되어 접근시 loadOrder 수행. 모든 메소드를 수행하기전 observe를 수행해야함t
    val chartList: MutableLiveData<MutableList<Chart>> by lazy {
        MutableLiveData<MutableList<Chart>>().apply {
            CoroutineScope(Dispatchers.IO).launch {
                postValue(loadChartOrder().toEmptyChart())
                //loadAllChartData() - lazy문은 시점 예측하기 좋지않음. 따라서 Fragment에서 로드 요청하더라도 거기로 옮기는게 좋을 것 같음. 여기서는 차트 순서 초기화만
            }
        }
    }

    //최초로 한번 모든 차트 데이터를 불러오는 기능
    fun loadAllChartData() {
        CoroutineScope(Dispatchers.IO).launch {
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
        if (chartList.value.isNullOrEmpty()) {
            Log.w(LOG_HEADER, "can't update recycler view. chart is empty.")
            return
        }

        val lastChart : MutableList<Chart> = chartList.value!! //마지막으로 업데이트된 차트
        val parsedChart = chartParser.parse(data, category) //파싱
        val categoryIndex = lastChart.indexOfFirst { it.type == category } //해당 enum을 가진 chart 탐색
        if (categoryIndex == -1) {
            //없는경우 - 순서 등록이 안되어있을때, 발생하지 않을것으로 사료됨
            Log.w(LOG_HEADER, "can't update recycler view. can't find chart index")
            return
        }
        lastChart[categoryIndex] = parsedChart //차트 업데이트
        chartList.postValue(lastChart) //notify

    }

}