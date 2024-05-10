package com.home.cdp2app.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.SaveBloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.heart.usecase.SaveHeartRate
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.health.sleep.usecase.SaveSleepHour
import com.home.cdp2app.util.livedata.Event
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

//dashboard에서 상세정보 클릭시 보여줄 액티비티의 vm
class ChartDetailViewModel(private val loadHeartRate: LoadHeartRate, private val loadBloodPressure: LoadBloodPressure, private val loadSleepHour: LoadSleepHour, private val saveHeartRate: SaveHeartRate, private val saveBloodPressure: SaveBloodPressure, private val saveSleepHour: SaveSleepHour, private val chartParser: ChartParser) {
    private val LOG_HEADER = "DASHBOARD_VIEWMODEL"
    val chartLiveData: MutableLiveData<Chart> = MutableLiveData()
    val saveLiveData: MutableLiveData<Event<ValidateStatus>> = MutableLiveData()

    // activity - chart 요청
    fun loadChart(category: HealthCategory) {
        val date: Instant = Instant.now()
        CoroutineScope(Dispatchers.IO).launch {
            val healthData: List<*> = when (category) {
                HealthCategory.HEART_RATE -> loadHeartRate(date)
                HealthCategory.BLOOD_PRESSURE_SYSTOLIC -> loadBloodPressure(date)
                HealthCategory.BLOOD_PRESSURE_DIASTOLIC -> loadBloodPressure(date)
                HealthCategory.SLEEP_HOUR -> loadSleepHour(date)
            }
            updateChart(healthData, category)
        }
    }

    fun saveHeartRate(date: Instant?, heartRate: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            // vm에서도 검증
            val status: ValidateStatus = if (heartRate <= 0) ValidateStatus.VALUE_ERROR //심박수가 음수인경우
            else if (date == null) ValidateStatus.FIELD_EMPTY //날짜가 비어있는 경우
            else {
                saveHeartRate(listOf(HeartRate(date, heartRate)))
                ValidateStatus.OK //저장후 ok
            }
            saveLiveData.postValue(Event(status)) //저장 성공함
        }
    }

    fun saveBloodPressure(date: Instant?, systolic: Double, diastolic: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            // vm에서도 검증
            val status: ValidateStatus = if (systolic <= 10 || diastolic <= 10) ValidateStatus.VALUE_ERROR //수축-이완기가 음수인경우
            else if (systolic < diastolic) ValidateStatus.VALUE_ERROR //이완기가 수축기보다 더 큰경우
            else if (date == null) ValidateStatus.FIELD_EMPTY //날짜가 비어있는 경우
            else {
                saveBloodPressure(BloodPressure(date, systolic, diastolic))
                ValidateStatus.OK //저장후 ok
            }
            saveLiveData.postValue(Event(status)) //저장 성공함
        }
    }

    fun saveSleepHour(date: Instant?, sleepHour: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            // vm에서도 검증
            val sleepMinutes = (sleepHour * 60).toLong() //1.5시간 -> 90분
            val status: ValidateStatus = if (sleepMinutes <= 0) ValidateStatus.VALUE_ERROR //수면시간이 음수인경우
            else if (date == null) ValidateStatus.FIELD_EMPTY //날짜가 비어있는 경우
            else {
                saveSleepHour(listOf(SleepHour(date, Duration.ofMinutes(sleepMinutes))))
                ValidateStatus.OK //저장후 ok
            }
            saveLiveData.postValue(Event(status)) //저장 성공함
        }
    }

    // 실제 livedata 갱신
    private suspend fun updateChart(data: List<*>, category: HealthCategory) {
        //차트가 초기화 되어 있지 않을경우 ignore
        if (data.isEmpty()) {
            Log.w(LOG_HEADER, "can't load chart. data is empty")
            return
        }
        val parsedChart = chartParser.parse(data, category) //파싱
        chartLiveData.postValue(parsedChart)

    }


}