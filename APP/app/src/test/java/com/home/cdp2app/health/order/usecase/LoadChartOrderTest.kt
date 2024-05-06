package com.home.cdp2app.health.order.usecase

import com.home.cdp2app.health.order.entity.ChartOrder
import com.home.cdp2app.health.order.repository.ChartOrderRepository
import com.home.cdp2app.health.order.type.HealthCategory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class LoadChartOrderTest {

    private val repository : ChartOrderRepository = mockk() //repository mock
    private val loadChartOrder = LoadChartOrder(repository)

    @Test
    fun TEST_LOAD_CHART_ORDER() {
        val order = ChartOrder(LinkedHashSet(HealthCategory.values().toList())) //return value
        coEvery { repository.loadOrder() } returns order

        runBlocking {
            val result = loadChartOrder()
            assertEquals(order, result)
        }
    }

}