package com.home.cdp2app.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.health.basic.type.Gender
import com.home.cdp2app.health.basic.usecase.LoadBasicInfo
import com.home.cdp2app.health.basic.usecase.SaveBasicInfo
import com.home.cdp2app.view.viewmodel.setting.BasicInfoViewModel
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit

class BasicInfoViewModelTest {

    private val loadBasicInfo : LoadBasicInfo = mockk() //mock basic load
    private val saveBasicInfo : SaveBasicInfo = mockk() //mock save
    private val viewModel : BasicInfoViewModel = BasicInfoViewModel(loadBasicInfo, saveBasicInfo)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule
    //VM.basicInfoLiveData에 접근할때 lazy로 로드 될지 확인
    @Test
    fun TEST_LOAD_INFO() {
        val response = BasicInfo(150.0, 70.0, Gender.WOMAN, false) //리턴할 값
        val defaultParam : CapturingSlot<Boolean> = slot() //파라미터 캡처용
        coEvery { loadBasicInfo(capture(defaultParam)) } returns response //load시 response 리턴
        assertEquals(response, viewModel.basicInfoLiveData.getOrAwaitValue(1, TimeUnit.SECONDS))
        assertTrue(defaultParam.captured) //기본값 불러오기 true, 위 await 수행시 capture도 이루어짐
    }

    @Test
    fun TEST_SAVE_INFO() {
        val captureData : CapturingSlot<BasicInfo> = slot()

        coEvery { saveBasicInfo(capture(captureData)) } returns mockk() // for capture
        viewModel.saveBasicInfo(145, 70, false, Gender.WOMAN) //세이브 요청
        coVerify(atLeast = 1) { saveBasicInfo(any()) }
        //값 비교
        val captured = captureData.captured
        assertEquals(145.0, captured.height, 0.0)
        assertEquals(70.0, captured.weight, 0.0)
        assertFalse(captured.isSmoking)
        assertEquals(Gender.WOMAN, captured.gender)

    }

    @Test
    fun TEST_NOTIFY_SAVE_COMPLETE() {
        coEvery { saveBasicInfo(any()) } returns mockk() // success save
        viewModel.saveBasicInfo(145, 70, false, Gender.WOMAN) //세이브 요청

        val event = viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //값 대기
        assertNotNull(event) //올바르게 notify가 되었는가
        val isSaveSuccess = event.getContent() //값 가져오기. 만약 null이라면 이미 consume 됨
        assertNotNull(isSaveSuccess)
        assertTrue(isSaveSuccess!!)

    }
}