package com.home.cdp2app.health.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HealthConnectAPITest {

    @Test
    fun TEST_CREATE_INTENT() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val response = HealthConnectAPI.createInstallSdkIntent(context)
        assertEquals(
            "market://details?id=${HealthConnectAPI.PROVIDER_PACKAGE_NAME}&url=healthconnect%3A%2F%2Fonboarding",
            response.data.toString())
        assertEquals(Intent.ACTION_VIEW, response.action)
        assertEquals("com.android.vending", response.`package`)
        assertEquals("com.home.cdp2app", response.extras?.getString("callerId"))
    }

}