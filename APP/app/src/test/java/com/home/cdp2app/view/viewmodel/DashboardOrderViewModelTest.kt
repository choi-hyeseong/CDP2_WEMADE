package com.home.cdp2app.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.health.order.entity.ChartOrder
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.health.order.usecase.LoadChartOrder
import com.home.cdp2app.health.order.usecase.SaveChartOrder
import com.home.cdp2app.view.viewmodel.setting.DashboardOrderViewModel
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.powermock.reflect.Whitebox
import java.util.concurrent.TimeUnit

class DashboardOrderViewModelTest {

    // test용 mock
    val loadChartOrder: LoadChartOrder = mockk()
    val saveChartOrder: SaveChartOrder = mockk()
    private val viewModel: DashboardOrderViewModel = DashboardOrderViewModel(loadChartOrder, saveChartOrder)

    private val order = ChartOrder(LinkedHashSet(listOf(HealthCategory.HEART_RATE, HealthCategory.BLOOD_PRESSURE_DIASTOLIC, HealthCategory.SLEEP_HOUR))) // test용 order
    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule


    @Before
    fun init() {
        coEvery { loadChartOrder() } returns order
    }

    @Test
    fun TEST_INIT_LOAD_ORDER() {
        //init시 chart order 잘 하는지 확인
        val result = viewModel.orderLivedata.getOrAwaitValue(1, TimeUnit.SECONDS)
        val orderField = Whitebox.getField(viewModel::class.java, "chartOrder").get(viewModel) as ChartOrder //reflection으로 private 필드 가져오기
        assertEquals(order, result)
        assertEquals(order, orderField)
    }

    @Test
    fun TEST_UPDATE() {
        viewModel.orderLivedata.getOrAwaitValue(1, TimeUnit.SECONDS) //for lazy loading

        val updateOrders = LinkedHashSet<HealthCategory>(listOf(HealthCategory.SLEEP_HOUR, HealthCategory.BLOOD_PRESSURE_DIASTOLIC)) //수면시간과 이완기 혈압만 갖고 있음. 테스트용이므로 검증 부분은 생략하고 새롭게 설정되는지만 확인
        viewModel.update(updateOrders)
        val orderField = Whitebox.getField(viewModel::class.java, "chartOrder").get(viewModel) as ChartOrder //reflection으로 private 필드 가져오기
        orderField.orders.forEachIndexed { index, category ->
            // to list로 변경해도 순서는 손실되지 않음
            assertEquals(updateOrders.toList()[index], category)
        }
    }

    @Test
    fun TEST_SAVE_SUCCESS() {
        viewModel.orderLivedata.getOrAwaitValue(1, TimeUnit.SECONDS) //for lazy loading
        val orderField = Whitebox.getField(viewModel::class.java, "chartOrder").get(viewModel) as ChartOrder //reflection으로 private 필드 가져오기
        val captureField: CapturingSlot<ChartOrder> = slot() //save할때 사용될 order slot
        coEvery { saveChartOrder(capture(captureField)) } returns mockk() //세이브할때 요청 캡쳐
        viewModel.save()
        Thread.sleep(500)
        val resultLiveData = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //결과값 livedata 확인
        assertEquals(orderField, captureField.captured)
        assertTrue(resultLiveData.getContent()!!)
    }

    @Test
    fun TEST_SAVE_FAIL() {
        viewModel.orderLivedata.getOrAwaitValue(1, TimeUnit.SECONDS) //for lazy loading
        coEvery { saveChartOrder(any()) } throws Exception("실패") //실패 throw
        viewModel.save()
        Thread.sleep(500)
        val resultLiveData = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //결과값 livedata 확인
        assertFalse(resultLiveData.getContent()!!)
    }


}