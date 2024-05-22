package com.home.cdp2app.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.main.setting.basicinfo.usecase.HasBasicInfo
import com.home.cdp2app.main.MainPagerViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit

class MainPagerViewModelTest {

    private val hasBasicInfo : HasBasicInfo = mockk() //mock 요청
    private val mainPagerViewModel = MainPagerViewModel(hasBasicInfo)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    @Test
    fun TEST_HAS_INFO() {
        // 유스케이스 통해 호출되는지 확인
        coEvery { hasBasicInfo() } returns false //info 안가지고 있음 리턴
        val result = mainPagerViewModel.checkHaveBasicInfo().getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = result.getContent()
        assertNotNull(content)
        assertFalse(content!!)
    }
}