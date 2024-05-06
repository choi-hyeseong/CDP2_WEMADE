package com.home.cdp2app.health.order.repository

import android.graphics.Paint.Cap
import com.home.cdp2app.health.order.entity.ChartOrder
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.memory.SharedPreferencesStorage
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class PreferenceOrderRepositoryTest {

    // sharedPreference mock
    private val preferencesStorage : SharedPreferencesStorage = mockk()
    // order repo
    private val orderRepository : PreferenceOrderRepository = PreferenceOrderRepository(preferencesStorage)

    @Test
    fun TEST_SAVE_ORDER() {
        // save check용 slot
        val keySlot : CapturingSlot<String> = slot()
        val saveSlot : CapturingSlot<ChartOrder> = slot()
        // capture
        coEvery { preferencesStorage.saveObject(capture(keySlot), capture(saveSlot)) } returns mockk()

        //for save
        val order = ChartOrder(LinkedHashSet(listOf(HealthCategory.HEART_RATE, HealthCategory.BLOOD_PRESSURE_DIASTOLIC, HealthCategory.BLOOD_PRESSURE_SYSTOLIC, HealthCategory.SLEEP_HOUR)))
        // test
        runBlocking {
            orderRepository.saveOrder(order)
            assertEquals("PREFERENCE_ORDER", keySlot.captured)
            assertEquals(order, saveSlot.captured)
        }
    }

    @Test
    fun TEST_THROW_ON_SAVE() {
        // 일부 카테고리 미 포함시 에러 발생
        coEvery { preferencesStorage.saveObject(any(), any()) } returns mockk()

        //for save
        val order = ChartOrder(LinkedHashSet(listOf(HealthCategory.HEART_RATE, HealthCategory.BLOOD_PRESSURE_SYSTOLIC, HealthCategory.SLEEP_HOUR))) // Diastolic 미포함
        // test
        runBlocking {
            assertThrows<IllegalArgumentException> { orderRepository.saveOrder(order) }

        }
    }

    @Test
    fun TEST_LOAD_ORDER() {
        // key capture
        val keySlot : CapturingSlot<String> = slot()
        // return
        val returnValue : ChartOrder = ChartOrder(LinkedHashSet(listOf(HealthCategory.HEART_RATE, HealthCategory.BLOOD_PRESSURE_DIASTOLIC, HealthCategory.BLOOD_PRESSURE_SYSTOLIC, HealthCategory.SLEEP_HOUR)))
        coEvery { preferencesStorage.loadObject(capture(keySlot), ChartOrder::class) } returns returnValue

        // test
        runBlocking {
            val result = orderRepository.loadOrder()
            assertEquals("PREFERENCE_ORDER", keySlot.captured)
            assertEquals(returnValue, result)
        }
    }

    @Test
    fun TEST_RETURN_DEFAULT() {
        coEvery { preferencesStorage.loadObject(any(), ChartOrder::class) } throws NoSuchElementException("존재하지 않음") //저장 안됨

        // test
        runBlocking {
            val result = orderRepository.loadOrder()
            assertEquals(ChartOrderRepository.DEFAULT, result) //default 반환

        }
    }
}