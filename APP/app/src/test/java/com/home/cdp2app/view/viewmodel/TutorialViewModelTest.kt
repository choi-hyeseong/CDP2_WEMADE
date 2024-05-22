package com.home.cdp2app.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.user.tutorial.usecase.SaveTutorialCompleted
import com.home.cdp2app.user.tutorial.view.viewmodel.TutorialViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit

class TutorialViewModelTest {
    private val saveTutorialCompleted : SaveTutorialCompleted = mockk()
    private val viewModel = TutorialViewModel(saveTutorialCompleted)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    @Test
    fun TEST_SIGN_OUT() {
        //어처피 auth token 지우고 확정적으로 true 리턴. 따라서 테스트 한번만 수행하면 될듯. 만약 개선한다면 deleteToken에서 결과값 리턴해서 그걸로 판단하면 좋을것 같음
        coEvery { saveTutorialCompleted() } returns mockk()
        val liveData = viewModel.saveTutorialEnded() //요청
        Thread.sleep(500) // Coroutine sleep
        val result = liveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertTrue(result)
    }
}