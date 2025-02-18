package com.home.cdp2app.view.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.health.bloodpressure.entity.BloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.main.setting.order.entity.ChartOrder
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.setting.order.usecase.LoadChartOrder
import com.home.cdp2app.health.sleep.entity.SleepHour
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.main.dashboard.chart.Chart
import com.home.cdp2app.main.dashboard.chart.parser.ChartParser
import com.home.cdp2app.main.dashboard.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.main.dashboard.view.viewmodel.DashboardViewModel
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.powermock.reflect.Whitebox
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

class DashboardViewModelTest {

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    private val loadChartOrder : LoadChartOrder = mockk() //mock order
    private val loadHeartRate : LoadHeartRate = mockk() //heart rate mock
    private val loadBloodPressure : LoadBloodPressure = mockk() //blood pressure mock
    private val loadSleepHour : LoadSleepHour = mockk() //sleep mock
    //chart parser
    private val chartParser = ChartParser(listOf(HeartRateChartMapper(), BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper(), SleepHourChartMapper()))
    private val viewModel = DashboardViewModel(loadChartOrder, loadHeartRate, loadBloodPressure, loadSleepHour, chartParser)
    private val order =  ChartOrder(LinkedHashSet(listOf(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, HealthCategory.HEART_RATE, HealthCategory.BLOOD_PRESSURE_DIASTOLIC, HealthCategory.SLEEP_HOUR)))

    @Before
    fun init() {
        // 위 coEvery, every는 테스트에 맞게 수정할 수 있음 - 덮어쓰기 가능
        mockkStatic(Log::class)
        every { Log.w(any(), any(String::class)) } returns 1 //Log.w mock
        //lazy하게 loadAll을 수행하기 때문에 일단 문제 없게 empty list 리턴하게
        coEvery { loadChartOrder() } returns order
        coEvery { loadHeartRate(any()) } returns mutableListOf()
        coEvery { loadBloodPressure(any()) } returns mutableListOf()
        coEvery { loadSleepHour(any()) } returns mutableListOf()
    }

    @After
    fun dispose() {
        unmockkAll()
    }

    @Test
    fun TEST_CHART_ORDER() {
        //lazy하게 접근하므로 observe하는 순간 로드 수행됨
        val chart = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        //reflection chart
        val fieldChart = Whitebox.getField(viewModel::class.java, "chartList").get(viewModel) as MutableList<Chart>
        //비교
        coVerify(atLeast = 1) { loadChartOrder() }
        assertEquals(chart, order.toEmptyChart()) //empty chart로 변환됨
        assertEquals(order.toEmptyChart(), fieldChart)
    }

    @Test
    fun TEST_HEART_RATE_EMPTY() {
        //차트 순서 불러오면 다음과 같은 데이터 반환 - 차트 순서에 맞게 셋팅이 됨
        coEvery { loadHeartRate(any()) } returns mutableListOf() //empty 반환

        //Log warn mocking
        val warnMessage : CapturingSlot<String> = slot()
        every { Log.w(any(), capture(warnMessage)) } returns 1

        runBlocking {
            viewModel.loadHeartRateChart(Instant.now())
            assertEquals("심박수 데이터가 비어있습니다.", warnMessage.captured)
        }
    }

    @Test
    fun TEST_LOAD_HEART_RATE() {
        val heartRate = listOf(HeartRate(Instant.now(), 150), HeartRate(Instant.now(), 145))
        coEvery { loadHeartRate(any()) } returns heartRate //정상적인 값 반환
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        runBlocking {
            viewModel.loadHeartRateChart(Instant.now())
            val result = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //차트 로드 대기
            val find = result.find { it.type == HealthCategory.HEART_RATE }
            assertNotNull(find) //찾아야함
            assertEquals(2, find!!.chartData.size)
            find.chartData.forEachIndexed { i, it ->
                assertEquals(heartRate[i].bpm.toDouble(), it.data, 0.0)
                assertEquals(heartRate[i].time, it.time)
            }
        }
    }



    @Test
    fun TEST_SLEEP_HOUR_EMPTY() {
        coEvery { loadSleepHour(any()) } returns mutableListOf() //empty 반환

        //Log warn mocking
        val warnMessage : CapturingSlot<String> = slot()
        every { Log.w(any(), capture(warnMessage)) } returns 1

        runBlocking {
            viewModel.loadSleepHourChart(Instant.now())
            assertEquals("수면시간 데이터가 비어있습니다.", warnMessage.captured)
        }
    }


    @Test
    fun TEST_LOAD_SLEEP_HOUR() {
        val sleepHour = listOf(SleepHour(Instant.now(), Duration.ofSeconds(10)), SleepHour(Instant.now(), Duration.ofHours(1)))
        coEvery { loadSleepHour(any()) } returns sleepHour //정상적인 값 반환
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        runBlocking {
            viewModel.loadSleepHourChart(Instant.now())
            val result = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //차트 로드 대기
            val find = result.find { it.type == HealthCategory.SLEEP_HOUR }
            assertNotNull(find) //찾아야함
            assertEquals(2, find!!.chartData.size)
            val sleepHourChartMapper = SleepHourChartMapper()
            find.chartData.forEachIndexed { i, it ->
                //차트에 표현하는 방식이 달라질 수 있어서 (수면시간) chartmapper로 접근
                assertEquals(sleepHourChartMapper.convertToChart(listOf(sleepHour[i])).chartData[0].data, it.data, 0.0)
                assertEquals(sleepHour[i].date, it.time)
            }
        }
    }


    @Test
    fun TEST_BLODD_PRESSURE_DIASTOLIC_EMPTY() {
        coEvery { loadBloodPressure(any()) } returns mutableListOf() //empty 반환

        //Log warn mocking
        val warnMessage : CapturingSlot<String> = slot()
        every { Log.w(any(), capture(warnMessage)) } returns 1

        runBlocking {
            viewModel.loadBloodPressureDiastolicChart(Instant.now())
            assertEquals("혈압 데이터가 비어있습니다.", warnMessage.captured)
        }
    }

    @Test
    fun TEST_LOAD_DIASTOLIC() {
        val bloodPressure = listOf(BloodPressure(Instant.now(), 145.5, 50.0), BloodPressure(Instant.now(),145.5, 70.0))
        coEvery { loadBloodPressure(any()) } returns bloodPressure //정상적인 값 반환
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        runBlocking {
            viewModel.loadBloodPressureDiastolicChart(Instant.now())
            val result = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //차트 로드 대기
            val find = result.find { it.type == HealthCategory.BLOOD_PRESSURE_DIASTOLIC }
            assertNotNull(find) //찾아야함
            assertEquals(2, find!!.chartData.size)
            find.chartData.forEachIndexed { i, it ->
                //차트에 표현하는 방식이 달라질 수 있어서 (수면시간) chartmapper로 접근
                assertEquals(bloodPressure[i].diastolic, it.data, 0.0)
                assertEquals(bloodPressure[i].date, it.time)
            }
        }
    }

    @Test
    fun TEST_BLOOD_PRESSURE_SYSTOLIC_EMPTY() {
        coEvery { loadBloodPressure(any()) } returns mutableListOf() //empty 반환

        //Log warn mocking
        val warnMessage : CapturingSlot<String> = slot()
        every { Log.w(any(), capture(warnMessage)) } returns 1

        runBlocking {
            viewModel.loadBloodPressureSystolicChart(Instant.now())
            assertEquals("혈압 데이터가 비어있습니다.", warnMessage.captured)
        }
    }


    @Test
    fun TEST_LOAD_SYSTOLIC() {
        val bloodPressure = listOf(BloodPressure(Instant.now(), 145.5, 50.0), BloodPressure(Instant.now(),145.5, 70.0))
        coEvery { loadBloodPressure(any()) } returns bloodPressure //정상적인 값 반환
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        runBlocking {
            viewModel.loadBloodPressureSystolicChart(Instant.now())
            val result = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //차트 로드 대기
            val find = result.find { it.type == HealthCategory.BLOOD_PRESSURE_SYSTOLIC }
            assertNotNull(find) //찾아야함
            assertEquals(2, find!!.chartData.size)
            find.chartData.forEachIndexed { i, it ->
                //systolic 비교
                assertEquals(bloodPressure[i].systolic, it.data, 0.0)
                assertEquals(bloodPressure[i].date, it.time)
            }
        }
    }

    @Test
    fun TEST_CHART_ORDER_EMPTY() {
        // order가 지정되어 있지 않아 초기화 되지 않은경우
        coEvery { loadChartOrder() } returns ChartOrder(LinkedHashSet())
        coEvery { loadHeartRate(any()) } returns mutableListOf(HeartRate(Instant.now(), 150)) //정상적인 값 반환
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기
        // for warn message capture
        val warnMessage : CapturingSlot<String> = slot()
        every { Log.w(any(), capture(warnMessage)) } returns 1

        runBlocking {
            viewModel.loadHeartRateChart(Instant.now())
            assertEquals("can't update recycler view. chart is empty.", warnMessage.captured)
        }
    }

    @Test
    fun TEST_CHART_ORDER_NOT_FOUND() {
        // 혈압에 대한 순서만 명시되어 있음
        coEvery { loadChartOrder() } returns ChartOrder(LinkedHashSet(listOf(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, HealthCategory.BLOOD_PRESSURE_SYSTOLIC)))
        coEvery { loadHeartRate(any()) } returns mutableListOf(HeartRate(Instant.now(), 150)) //정상적인 값 반환
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기
        val warnMessage : CapturingSlot<String> = slot()
        every { Log.w(any(), capture(warnMessage)) } returns 1

        runBlocking {
            viewModel.loadHeartRateChart(Instant.now())
            assertEquals("can't update recycler view. can't find chart index", warnMessage.captured)
        }
    }

    @Test
    fun TEST_REQUEST_SYNC_HEART_RATE() {
        //order mock
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        val heartRate = listOf(HeartRate(Instant.now(), 150), HeartRate(Instant.now(), 145))
        coEvery { loadHeartRate(any()) } returns heartRate //정상적인 값 반환

        viewModel.requestSync(HealthCategory.HEART_RATE) //심박수로 호출
        Thread.sleep(500) //requestSync의 경우 CoroutineScope를 열기 때문에 지연을 걸어야 정상적인 값을 가져올 수 있을것으로 기대

        //toast event check
        val event = viewModel.toastLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //이벤트 비교
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(HealthCategory.HEART_RATE, content)
    }

    @Test
    fun TEST_REQUEST_SYNC_SLEEP_HOUR() {
        //order mock
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        val sleepHour = listOf(SleepHour(Instant.now(), Duration.ofHours(1)))
        coEvery { loadSleepHour(any()) } returns sleepHour //정상적인 값 반환

        viewModel.requestSync(HealthCategory.SLEEP_HOUR) //심박수로 호출
        Thread.sleep(500) //requestSync의 경우 CoroutineScope를 열기 때문에 지연을 걸어야 정상적인 값을 가져올 수 있을것으로 기대

        //toast event check
        val event = viewModel.toastLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //이벤트 비교
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(HealthCategory.SLEEP_HOUR, content)
    }

    @Test
    fun TEST_REQUEST_SYNC_BLOOD_SYSTOLIC() {
        //order mock
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        val bloodPressure = listOf(BloodPressure(Instant.now(), 145.0, 150.0))
        coEvery { loadBloodPressure(any()) } returns bloodPressure //정상적인 값 반환

        viewModel.requestSync(HealthCategory.BLOOD_PRESSURE_SYSTOLIC) //심박수로 호출
        Thread.sleep(500) //requestSync의 경우 CoroutineScope를 열기 때문에 지연을 걸어야 정상적인 값을 가져올 수 있을것으로 기대

        //toast event check
        val event = viewModel.toastLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //이벤트 비교
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, content)
    }

    @Test
    fun TEST_REQUEST_SYNC_BLOOD_DIASTOLIC() {
        //order mock
        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //order init 대기

        val bloodPressure = listOf(BloodPressure(Instant.now(), 145.0, 150.0))
        coEvery { loadBloodPressure(any()) } returns bloodPressure //정상적인 값 반환

        viewModel.requestSync(HealthCategory.BLOOD_PRESSURE_DIASTOLIC) //심박수로 호출
        Thread.sleep(500) //requestSync의 경우 CoroutineScope를 열기 때문에 지연을 걸어야 정상적인 값을 가져올 수 있을것으로 기대

        //toast event check
        val event = viewModel.toastLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //이벤트 비교
        val content = event.getContent()
        assertNotNull(content)
        assertEquals(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, content)
    }

    @Test
    fun TEST_LOAD_ALL() {
        //임시 데이터 제공
        coEvery { loadHeartRate(any()) } returns mutableListOf(HeartRate(Instant.now(), 150))
        coEvery { loadBloodPressure(any()) } returns mutableListOf(BloodPressure(Instant.now(), 140.0, 70.0))
        coEvery { loadSleepHour(any()) } returns mutableListOf(SleepHour(Instant.now(), Duration.ofHours(1)))

        viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //loadAll 호출 (lazy)
        //CoroutineScope sleep
        Thread.sleep(500)
        //호출됐는지 확인
        coVerify(atLeast = 1) { loadHeartRate(any()) }
        coVerify(atLeast = 1) { loadSleepHour(any()) }
        coVerify(atLeast = 1) { loadBloodPressure(any()) }
        //reflection chart
        val fieldChart = Whitebox.getField(viewModel::class.java, "chartList").get(viewModel) as MutableList<Chart>
        val chart = viewModel.chartLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        //차트의 모든값이 비어있어선 안됨 (값이 다 있음)
        chart.forEachIndexed { index, it ->
            assertEquals(1, it.chartData.size)
            assertEquals(1, fieldChart[index].chartData.size)
        }

    }

}