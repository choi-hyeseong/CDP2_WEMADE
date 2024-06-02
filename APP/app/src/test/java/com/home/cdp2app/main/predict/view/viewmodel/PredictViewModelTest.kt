package com.home.cdp2app.main.predict.view.viewmodel

import android.graphics.Paint.Cap
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.getOrAwaitValue
import com.home.cdp2app.common.network.type.NetworkStatus
import com.home.cdp2app.main.predict.api.dto.PredictResponseDTO
import com.home.cdp2app.main.predict.entity.PredictResult
import com.home.cdp2app.main.predict.usecase.GetCachePredictResult
import com.home.cdp2app.main.predict.usecase.PredictUseCase
import com.home.cdp2app.main.predict.usecase.SaveCachePredictResult
import com.home.cdp2app.user.sign.api.dto.RegisterResponse
import com.home.cdp2app.user.token.usecase.DeleteAuthToken
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.StatusCode
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.powermock.reflect.Whitebox
import retrofit2.Response
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class PredictViewModelTest {

    private val deleteAuthToken : DeleteAuthToken = mockk()
    private val predictUseCase : PredictUseCase = mockk()
    private val saveCachePredictResult : SaveCachePredictResult = mockk()
    private val getCachePredictResult : GetCachePredictResult = mockk()
    private val viewModel = PredictViewModel(deleteAuthToken, predictUseCase, saveCachePredictResult, getCachePredictResult)

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule() //livedata testing rule

    @Before
    fun init() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 1 //log mock
    }

    @After
    fun final() {
        unmockkAll()
    }


    @Test
    fun TEST_LOAD_CACHE_SUCCESS() {
        // 저장된 캐시 불러오는지 확인
        coEvery { getCachePredictResult() } returns PredictResult(15.0) //저장된 캐시값

        val result = viewModel.predictLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) //기본적으로 lazy로 받아온 MutableLiveData 객체를 받음 -> 이걸 await하므로 also문 확인가능
        assertEquals(15.0, result.percent)
    }


    @Test
    fun TEST_LOAD_CACHE_FAIL() {
        // 저장된 캐시가 없는경우
        coEvery { getCachePredictResult() } returns null //없음
        assertThrows(TimeoutException::class.java) { viewModel.predictLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)}
    }

    @Test
    fun TEST_PREDICT_SUCCESS() {
        val response = PredictResponseDTO(0.5f)


        val captureBool : CapturingSlot<Boolean> = slot()
        val captureCache : CapturingSlot<PredictResult> = slot()
        coEvery { predictUseCase(capture(captureBool)) } returns ApiResponse.of { Response.success(response) } //50% 확률
        coEvery { saveCachePredictResult(capture(captureCache)) } returns mockk()
        // for livedata init
        coEvery { getCachePredictResult() } returns null //없음
        assertThrows(TimeoutException::class.java) { viewModel.predictLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)}

        runBlocking {
            viewModel.requestPredict(false)

            val network = viewModel.networkStatus.getOrAwaitValue(1, TimeUnit.SECONDS)
            val content = network.getContent()
            assertNotNull(content)
            assertEquals(NetworkStatus.OK, content)
            assertFalse(captureBool.captured)

            val result = viewModel.predictLiveData.getOrAwaitValue(1, TimeUnit.SECONDS)

            assertEquals(response.toEntity(), captureCache.captured)
            assertEquals(response.toEntity(), result)
        }
    }

    @Test
    fun TEST_PREDICT_ERROR_NORMAL() {
        //일반 error인경우
        val warnCapture : CapturingSlot<String> = slot()

        coEvery { predictUseCase(any()) } returns ApiResponse.Failure.Error(Response.error(500, ResponseBody.create("application/json".toMediaTypeOrNull()!!, "")))
        every { Log.w(any(), capture(warnCapture)) } returns 1

        runBlocking {
            viewModel.requestPredict(false)
            Thread.sleep(500)
            val network = viewModel.networkStatus.getOrAwaitValue(1, TimeUnit.SECONDS)
            val content = network.getContent()
            assertNotNull(content)
            assertEquals(NetworkStatus.INTERNAL_ERROR, content)

            assertTrue(warnCapture.captured.contains("Encountered Retrofit Error. Status : ${StatusCode.InternalServerError.toString()}")) //warn check
        }
    }

    @Test
    fun TEST_PREDICT_ERROR_UNAUTHORIZED() {
        //일반 error인경우
        val warnCapture : CapturingSlot<String> = slot()
        // bad request로 token 제거 호출하는지 확인
        coEvery { predictUseCase(any()) } returns ApiResponse.Failure.Error(Response.error(401, ResponseBody.create("application/json".toMediaTypeOrNull()!!, ""))) //un auth
        coEvery { deleteAuthToken() } returns mockk() //for execute
        every { Log.w(any(), capture(warnCapture)) } returns 1

        runBlocking {
            viewModel.requestPredict(false)
            Thread.sleep(500)
            val network = viewModel.networkStatus.getOrAwaitValue(1, TimeUnit.SECONDS)
            val content = network.getContent()
            assertNotNull(content)
            assertEquals(NetworkStatus.UNAUTHORIZED, content)

            coVerify(atLeast = 1) { deleteAuthToken() } //token 제거 확인
            assertTrue(warnCapture.captured.contains("Encountered Retrofit Error. Status : ${StatusCode.Unauthorized.toString()}")) //warn check
        }
    }

    @Test
    fun TEST_CANCEL() {
        val warnCapture : CapturingSlot<String> = slot()
        // bad request로 token 제거 호출하는지 확인
        coEvery { predictUseCase(any()) }.answers {
            Thread.sleep(5000) //for delay
            ApiResponse.Failure.Error(Response.error(401, ResponseBody.create("application/json".toMediaTypeOrNull()!!, ""))) //un auth
        }
        coEvery { deleteAuthToken() } returns mockk() //for execute
        every { Log.w(any(), capture(warnCapture)) } returns 1

        runBlocking {
            viewModel.requestPredict(false)
            val job = Whitebox.getField(viewModel::class.java, "job").get(viewModel) as Job //job field 가져오기
            assertFalse(job.isCancelled)
            viewModel.cancel()
            assertTrue(job.isCancelled)
        }
    }
}