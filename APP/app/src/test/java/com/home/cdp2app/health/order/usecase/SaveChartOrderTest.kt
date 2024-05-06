package com.home.cdp2app.health.order.usecase

import com.home.cdp2app.health.order.entity.ChartOrder
import com.home.cdp2app.health.order.repository.ChartOrderRepository
import com.home.cdp2app.health.order.type.HealthCategory
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class SaveChartOrderTest {

    private val chartOrderRepository : ChartOrderRepository = mockk() //mock
    private val saveChartOrder = SaveChartOrder(chartOrderRepository)

    @Test
    fun TEST_SAVE_ORDER() {
        val slot : CapturingSlot<ChartOrder> = slot() //slot
        coEvery { chartOrderRepository.saveOrder(capture(slot)) } returns mockk() //capture

        val param = ChartOrder(LinkedHashSet(HealthCategory.values().toList()))
        runBlocking {
            saveChartOrder(param)
            assertEquals(param, slot.captured)
        }
    }

    @Test
    fun TEST_THROWS_ORDER() {
        coEvery { chartOrderRepository.saveOrder(any()) } throws IllegalArgumentException("~")

        runBlocking {
            assertThrows<IllegalArgumentException> {
                saveChartOrder(ChartOrder(LinkedHashSet(hashSetOf(HealthCategory.SLEEP_HOUR))))
            }
        }
    }

}