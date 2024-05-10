package com.home.cdp2app.view.dialog.factory

import androidx.fragment.app.DialogFragment
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.view.dialog.BloodPressureDialog
import com.home.cdp2app.view.dialog.HeartRateDialog
import com.home.cdp2app.view.dialog.SleepHourDialog
import com.home.cdp2app.view.dialog.validator.validate.blood.BloodPressureViewValidator
import com.home.cdp2app.view.dialog.validator.validate.heart.HeartRateViewValidator
import com.home.cdp2app.view.dialog.validator.validate.sleep.SleepHourViewValidator
import com.home.cdp2app.view.viewmodel.ChartDetailViewModel

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