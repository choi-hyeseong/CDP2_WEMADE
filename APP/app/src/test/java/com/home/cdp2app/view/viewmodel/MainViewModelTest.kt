package com.home.cdp2app.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.user.auth.usecase.DeleteAuthToken
import com.home.cdp2app.user.auth.usecase.HasAuthToken
import com.home.cdp2app.user.tutorial.usecase.CheckTutorialCompleted
import com.home.cdp2app.view.viewmodel.setting.SettingViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit

class MainViewModelTest {
    private val checkTutorialCompleted : CheckTutorialCompleted = mockk()
    private val hasAuthToken : HasAuthToken = mockk()
    private val viewModel = MainViewModel(hasAuthToken, checkTutorialCompleted)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    //token을 가진경우
    @Test
    fun TEST_HAVE_TOKEN() {
        coEvery { hasAuthToken() } returns true
        val liveData = viewModel.checkAuthToken() //요청
        Thread.sleep(500) // Coroutine sleep
        val result = liveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertTrue(result)
    }

    @Test
    fun TEST_NOT_HAVE_TOKEN() {
        coEvery { hasAuthToken() } returns false
        val liveData = viewModel.checkAuthToken() //요청
        Thread.sleep(500) // Coroutine sleep
        val result = liveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertFalse(result)
    }

    @Test
    fun TEST_TUTORIAL_ENDED() {
        coEvery { checkTutorialCompleted() } returns true
        val liveData = viewModel.checkTutorialStatus() //요청
        Thread.sleep(500) // Coroutine sleep
        val result = liveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertTrue(result)
    }

    @Test
    fun TEST_TUTORIAL_NOT_ENDED() {
        coEvery { checkTutorialCompleted() } returns false
        val liveData = viewModel.checkTutorialStatus() //요청
        Thread.sleep(500) // Coroutine sleep
        val result = liveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        assertFalse(result)
    }
}