package com.home.cdp2app.view.viewmodel.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.main.setting.SettingViewModel
import com.home.cdp2app.user.token.usecase.DeleteAuthToken
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit

class SettingViewModelTest {

    private val deleteAuthToken : DeleteAuthToken = mockk()
    private val viewModel = SettingViewModel(deleteAuthToken)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    @Test
    fun TEST_SIGN_OUT() {
        //어처피 auth token 지우고 확정적으로 true 리턴. 따라서 테스트 한번만 수행하면 될듯. 만약 개선한다면 deleteToken에서 결과값 리턴해서 그걸로 판단하면 좋을것 같음
        coEvery { deleteAuthToken() } returns mockk()
        val liveData = viewModel.signOut() //요청
        Thread.sleep(500) // Coroutine sleep
        val result = liveData.getOrAwaitValue(1, TimeUnit.SECONDS)
        val content = result.getContent()
        assertNotNull(content)
        assertTrue(content!!)
    }
}