package com.home.cdp2app.view.dialog.factory

import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.dashboard.view.dialog.blood.BloodPressureDialog
import com.home.cdp2app.main.dashboard.view.dialog.heart.HeartRateDialog
import com.home.cdp2app.main.dashboard.view.dialog.sleep.SleepHourDialog
import com.home.cdp2app.main.dashboard.view.dialog.factory.DialogFactory
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DialogFactoryTest {

    //실제 값 사용하지는 않으므로 mock 처리
    private val factory : DialogFactory = DialogFactory(mockk(), mockk(), mockk())

    @Test
    fun TEST_PROVIDE_HEART_RATE() {
        assertEquals(HeartRateDialog::class, factory.provide(HealthCategory.HEART_RATE, mockk())::class)
    }

    @Test
    fun TEST_PROVIDE_BLOOD_SYSTOLIC() {
        //수축기 - 혈압 다이얼로그 제공
        assertEquals(BloodPressureDialog::class, factory.provide(HealthCategory.BLOOD_PRESSURE_SYSTOLIC, mockk())::class)
    }

    @Test
    fun TEST_PROVIDE_BLOOD_DIASTOLIC() {
        //이완기 - 혈압 다이얼로그 제공
        assertEquals(BloodPressureDialog::class, factory.provide(HealthCategory.BLOOD_PRESSURE_DIASTOLIC, mockk())::class)
    }

    @Test
    fun TEST_PROVIDE_SLEEP_HOUR() {
        // 수면시간 다이얼로그 제공
        assertEquals(SleepHourDialog::class, factory.provide(HealthCategory.SLEEP_HOUR, mockk())::class)
    }
}