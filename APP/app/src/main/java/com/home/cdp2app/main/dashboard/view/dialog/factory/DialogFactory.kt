package com.home.cdp2app.main.dashboard.view.dialog.factory

import androidx.fragment.app.DialogFragment
import com.home.cdp2app.main.dashboard.view.dialog.blood.BloodPressureDialog
import com.home.cdp2app.main.dashboard.view.dialog.blood.validator.BloodPressureViewValidator
import com.home.cdp2app.main.dashboard.view.dialog.heart.HeartRateDialog
import com.home.cdp2app.main.dashboard.view.dialog.heart.validator.HeartRateViewValidator
import com.home.cdp2app.main.dashboard.view.dialog.sleep.SleepHourDialog
import com.home.cdp2app.main.dashboard.view.dialog.sleep.validator.SleepHourViewValidator
import com.home.cdp2app.main.dashboard.view.viewmodel.ChartDetailViewModel
import com.home.cdp2app.main.setting.order.type.HealthCategory

/**
 * 건강 정보를 저장하는 다이얼로그를 생성하는 팩토리 입니다.
 * @property heartRateViewValidate 심박수 다이얼로그를 검증하는 검증기입니다.
 */
class DialogFactory(
        private val heartRateViewValidate: HeartRateViewValidator,
        private val bloodPressureViewValidator: BloodPressureViewValidator,
        private val sleepHourViewValidator: SleepHourViewValidator
) {

    fun provide(category: HealthCategory, viewModel: ChartDetailViewModel) : DialogFragment {
        return when (category) {
            HealthCategory.HEART_RATE -> HeartRateDialog(heartRateViewValidate, viewModel)
            HealthCategory.BLOOD_PRESSURE_SYSTOLIC -> BloodPressureDialog(bloodPressureViewValidator, viewModel)
            HealthCategory.BLOOD_PRESSURE_DIASTOLIC -> BloodPressureDialog(bloodPressureViewValidator, viewModel)
            HealthCategory.SLEEP_HOUR -> SleepHourDialog(sleepHourViewValidator, viewModel)
        }
    }
}