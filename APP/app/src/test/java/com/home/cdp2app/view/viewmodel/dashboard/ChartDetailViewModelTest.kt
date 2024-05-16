package com.home.cdp2app.view.viewmodel.dashboard

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.SaveBloodPressure
import com.home.cdp2app.health.bloodpressure.valid.BloodPressureValidator
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.heart.usecase.SaveHeartRate
import com.home.cdp2app.health.heart.valid.HeartRateValidator
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.health.sleep.usecase.SaveSleepHour
import com.home.cdp2app.health.sleep.valid.SleepHourValidator
import com.home.cdp2app.valid.type.ValidateStatus
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.math.log

class ChartDetailViewModelTest {

    // test용 mock
    private val loadHeartRate : LoadHeartRate = mockk()
    private val loadBloodPressure : LoadBloodPressure = mockk()
    private val loadSleepHour : LoadSleepHour = mockk()
    private val saveHeartRate : SaveHeartRate = mockk()
    private val saveBloodPressure : SaveBloodPressure = mockk()
    private val saveSleepHour : SaveSleepHour = mockk()
    private val heartRateValidator : HeartRateValidator = mockk()
    private val sleepHourValidator : SleepHourValidator = mockk()
    private val bloodPressureValidator : BloodPressureValidator = mockk()
    private val chartParser = ChartParser(listOf(HeartRateChartMapper(), SleepHourChartMapper(), BloodPressureSystolicChartMapper(), BloodPressureDiastolicChartMapper()))
    private val viewModel = ChartDetailViewModel(loadHeartRate, loadBloodPressure, loadSleepHour, saveHeartRate, saveBloodPressure, saveSleepHour, heartRateValidator, sleepHourValidator, bloodPressureValidator, chartParser)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule


    @Test
    fun TEST_SAVE_HEART_SUCCESS() {
        val saveCapture : CapturingSlot<List<HeartRate>> = slot()
        coEvery { heartRateValidator.validate(any(), any()) } returns ValidateStatus.OK //실제 validator를 사용하지 않고 mock을 한 뒤 OK 반환하게
        coEvery { saveHeartRate(capture(saveCapture)) } returns mockk() //성공하게

        //dialog part, dateTimeutil 변환 문제 때문에 date형식은 올바르게 되어야 할듯
        viewModel.saveHeartRate("2024-05-15 15:00", "140")
        Thread.sleep(500) //Coroutine Scope sleep
        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(1, saveCapture.captured.size)
        assertEquals(140, saveCapture.captured[0].bpm)
        assertEquals(ValidateStatus.OK, content)
    }

    @Test
    fun TEST_SAVE_HEART_FAIL() {
        coEvery { heartRateValidator.validate(any(), any()) } returns ValidateStatus.FIELD_EMPTY //field empty로 인해 저장 안됨

        viewModel.saveHeartRate("2024-05-15 15:00", null)
        Thread.sleep(500) //Coroutine Scope sleep
        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(ValidateStatus.FIELD_EMPTY, content)
    }

    @Test
    fun TEST_SAVE_BLOOD_PRESSURE_SUCCESS() {
        val saveCapture : CapturingSlot<BloodPressure> = slot()
        coEvery { bloodPressureValidator.validate(any(), any(), any()) } returns ValidateStatus.OK //실제 validator를 사용하지 않고 mock을 한 뒤 OK 반환하게
        coEvery { saveBloodPressure(capture(saveCapture)) } returns mockk() //성공하게

        //dialog part, dateTimeutil 변환 문제 때문에 date형식은 올바르게 되어야 할듯. 만약 이것도 해결할려면 뭐.. mockStatic 수행해야할듯
        viewModel.saveBloodPressure("2024-05-15 15:00", "120", "70")
        Thread.sleep(500) //Coroutine Scope sleep
        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(120.0, saveCapture.captured.systolic, 0.0)
        assertEquals(70.0, saveCapture.captured.diastolic, 0.0)
        assertEquals(ValidateStatus.OK, content)
    }

    @Test
    fun TEST_SAVE_BLOOD_PRESSURE_FAIL() {
        coEvery { bloodPressureValidator.validate(any(), any(), any()) } returns ValidateStatus.FIELD_EMPTY //field empty로 인해 저장 안됨

        viewModel.saveBloodPressure("2024-05-15 15:00", null, null)
        Thread.sleep(500) //Coroutine Scope sleep
        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(ValidateStatus.FIELD_EMPTY, content)
    }

    @Test
    fun TEST_SAVE_SLEEP_HOUR_SUCCESS() {
        val saveCapture : CapturingSlot<List<SleepHour>> = slot()
        coEvery { sleepHourValidator.validate(any(), any()) } returns ValidateStatus.OK //실제 validator를 사용하지 않고 mock을 한 뒤 OK 반환하게
        coEvery { saveSleepHour(capture(saveCapture)) } returns mockk() //성공하게

        //dialog part, dateTimeutil 변환 문제 때문에 date형식은 올바르게 되어야 할듯
        viewModel.saveSleepHour("2024-05-15 15:00", "3")
        Thread.sleep(500) //Coroutine Scope sleep
        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(1, saveCapture.captured.size)
        assertEquals(3.0, saveCapture.captured[0].duration.toMinutes() / 60.0, 0.0) //저장할때는 시간으로 들어가기 때문에 분단위로 변경한뒤 나눠서 비교
        assertEquals(ValidateStatus.OK, content)
    }

    @Test
    fun TEST_SAVE_SLEEP_HOUR_FAIL() {
        coEvery { sleepHourValidator.validate(any(), any()) } returns ValidateStatus.FIELD_EMPTY //field empty로 인해 저장 안됨

        viewModel.saveSleepHour("2024-05-15 15:00", null)
        Thread.sleep(500) //Coroutine Scope sleep
        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(ValidateStatus.FIELD_EMPTY, content)
    }

    @Test
    fun TEST_UPDATE_CHART_FAIL() {
        mockkStatic(Log::class)
        val logCapture : CapturingSlot<String> = slot()
        coEvery { Log.w(any(), capture(logCapture)) } returns 1
        coEvery { loadHeartRate(any()) } returns mutableListOf() //empty chart data

        viewModel.loadChart(HealthCategory.HEART_RATE) //심박수 로드 요청
        Thread.sleep(500) //CoroutineScope sleep
        assertEquals("can't load chart. data is empty", logCapture.captured)
        assertThrows(TimeoutException::class.java) {
            viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //데이터 못불러옴
        }
    }
    @Test
    fun TEST_UPDATE_HEART_RATE() {
        coEvery { loadHeartRate(any()) } returns mutableListOf(HeartRate(Instant.now(), 140))
        viewModel.loadChart(HealthCategory.HEART_RATE)
        Thread.sleep(500) //coroutine sleep

        val chart = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertEquals(HealthCategory.HEART_RATE, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(140.0, chart.chartData[0].data, 0.0)
    }

    @Test
    fun TEST_UPDATE_SYSTOLIC() {
        coEvery { loadBloodPressure(any()) } returns mutableListOf(BloodPressure(Instant.now(), 120.0, 70.0))
        viewModel.loadChart(HealthCategory.BLOOD_PRESSURE_SYSTOLIC)
        Thread.sleep(500) //coroutine sleep

        val chart = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertEquals(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(120.0, chart.chartData[0].data, 0.0)
    }

    @Test
    fun TEST_UPDATE_DIASTOLIC() {
        coEvery { loadBloodPressure(any()) } returns mutableListOf(BloodPressure(Instant.now(), 120.0, 70.0))
        viewModel.loadChart(HealthCategory.BLOOD_PRESSURE_DIASTOLIC)
        Thread.sleep(500) //coroutine sleep

        val chart = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertEquals(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(70.0, chart.chartData[0].data, 0.0)
    }

    @Test
    fun TEST_UPDATE_SLEEP_HOUR() {
        coEvery { loadSleepHour(any()) } returns mutableListOf(SleepHour(Instant.now(), Duration.ofMinutes(90))) //1.5시간
        viewModel.loadChart(HealthCategory.SLEEP_HOUR)
        Thread.sleep(500) //coroutine sleep

        val chart = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertEquals(HealthCategory.SLEEP_HOUR, chart.type)
        assertEquals(1, chart.chartData.size)
        assertEquals(1.5, chart.chartData[0].data, 0.0)
    }
}