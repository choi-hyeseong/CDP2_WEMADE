package com.home.cdp2app.health.healthconnect.component

import android.content.Context
import android.os.Build
import androidx.health.connect.client.HealthConnectClient
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.powermock.reflect.Whitebox

class HealthConnectAPITest {

    /*
    * SDK Status 테스트 파트. Android SDK Version이 낮아서 Exception 생기는 부분은 현재 Min SDK가 28로
    * HealthConnect API 충분히 지원하는 부분이라 발생할 가능성이 없어서 테스트하지 않음.
     */
    @Test
    fun TEST_SDK_NOT_SUPPORTED() {
        //static mock
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        val context = mockk<Context>()
        every { HealthConnectClient.sdkStatus(any()) } returns HealthConnectClient.SDK_UNAVAILABLE
        assertEquals(HealthConnectStatus.NOT_SUPPORTED, HealthConnectAPI.getSdkStatus(context))
        unmockkAll()
    }

    @Test
    fun TEST_SDK_REQUIRE_INSTALL_OR_UPDATE() {
        //static mock
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        val context = mockk<Context>()
        every { HealthConnectClient.sdkStatus(any()) } returns HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
        assertEquals(HealthConnectStatus.REQUIRE_INSTALL, HealthConnectAPI.getSdkStatus(context))
        unmockkAll()
    }

    @Test
    fun TEST_SDK_OK() {
        //static mock
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        val context = mockk<Context>()
        every { HealthConnectClient.sdkStatus(any()) } returns HealthConnectClient.SDK_AVAILABLE
        assertEquals(HealthConnectStatus.OK, HealthConnectAPI.getSdkStatus(context))
        unmockkAll()
    }

    /*
     * HealthConnect 객체 가져오는 부분 테스트, 설치 안된부분과 버젼 낮은 2가지 Exception 경우에 한해 체크함.
     * 그 외의 경우는 Context를 정상적으로 가져와서 HealthConnectClient를 반환함
     */

    @Test
    fun TEST_FAILED_TO_GET_CLIENT_VERSION_LOW() {
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        //최소 28버젼보다 낮은 27버젼
        Whitebox.setInternalState(
            Build.VERSION::class.java, "SDK_INT", 27)
        val context = mockk<Context>()
        org.junit.jupiter.api.assertThrows<UnsupportedOperationException> { HealthConnectAPI.getHealthConnectClient(context) }
        unmockkAll()
    }

    @Test
    fun TEST_FAILED_TO_GET_CLIENT_NOT_INSTALLED() {
        mockkStatic(HealthConnectClient::class)
        mockkStatic(HealthConnectClient.Companion::class)
        mockkObject(HealthConnectClient)
        Whitebox.setInternalState(
            Build.VERSION::class.java, "SDK_INT", 28)
        val context = mockk<Context>()
        every { HealthConnectClient.isProviderAvailable(any(), any()) } returns false //HealthConnect의 설치 여부를 가져오지 못했을때
        every { HealthConnectClient.sdkStatus(any()) } returns HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
        org.junit.jupiter.api.assertThrows<IllegalStateException> { HealthConnectAPI.getHealthConnectClient(context) }
        unmockkAll()
    }

    //intent 테스트 부분은 androidTest 활용해야 하거나 해야할듯..


}