package com.home.cdp2app.view.viewmodel.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.util.livedata.Event
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.valid.type.ValidateStatus
import com.home.cdp2app.health.bloodpressure.valid.BloodPressureValidator
import com.home.cdp2app.health.heart.valid.HeartRateValidator
import com.home.cdp2app.health.sleep.valid.SleepHourValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

//dashboard에서 상세정보 클릭시 보여줄 액티비티의 vm
class ChartDetailViewModel(private val loadHeartRate: LoadHeartRate,
                           private val loadBloodPressure: LoadBloodPressure,
                           private val loadSleepHour: LoadSleepHour,
                           private val saveHeartRate: SaveHeartRate,
                           private val saveBloodPressure: SaveBloodPressure,
                           private val saveSleepHour: SaveSleepHour,
                           private val heartRateValidator: HeartRateValidator,
                           private val sleepHourValidator: SleepHourValidator,
                           private val bloodPressureValidator: BloodPressureValidator,
                           private val chartParser: ChartParser) : ViewModel() {
    private val LOG_HEADER = "CHART_DETAIL_VIEWMODEL"
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

    fun saveHeartRate(date: String?, heartRate: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            // vm에서도 검증
            val status: ValidateStatus = heartRateValidator.validate(date, heartRate)
            if (status == ValidateStatus.OK)
                saveHeartRate(listOf(HeartRate(DateTimeUtil.convertToDate(date)!!, heartRate!!.toLong())))
            saveLiveData.postValue(Event(status)) //저장 성공함
        }
    }

    fun saveBloodPressure(date: String?, systolic: String?, diastolic: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            // vm에서도 검증
            val status: ValidateStatus = bloodPressureValidator.validate(date, systolic, diastolic)
            if (status == ValidateStatus.OK)
                saveBloodPressure(BloodPressure(DateTimeUtil.convertToDate(date)!!, systolic!!.toDouble(), diastolic!!.toDouble())) //ok면 검증완료
            saveLiveData.postValue(Event(status)) //저장 성공함
        }
    }

    fun saveSleepHour(date: String?, sleepHour: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            // vm에서도 검증
            val status: ValidateStatus = sleepHourValidator.validate(date, sleepHour)
            if (status == ValidateStatus.OK)
                saveSleepHour(listOf(SleepHour(DateTimeUtil.convertToDate(date)!!, Duration.ofMinutes((sleepHour!!.toDouble() * 60.0).toLong())))) //곱하기로 해야함. 1.5 * 60 = 90
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