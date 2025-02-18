package com.home.cdp2app.main.dashboard.view.dialog.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.home.cdp2app.R
import com.home.cdp2app.common.throttle.setThrottleClickListener
import com.home.cdp2app.common.util.date.DateTimeUtil
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.home.cdp2app.databinding.DialogSleephourBinding
import com.home.cdp2app.main.dashboard.view.dialog.AbstractHealthDialog
import com.home.cdp2app.main.dashboard.view.dialog.DateTimePickerCallback
import com.home.cdp2app.main.dashboard.view.dialog.sleep.validator.SleepHourViewValidator
import com.home.cdp2app.main.dashboard.view.viewmodel.ChartDetailViewModel
import com.home.cdp2app.main.setting.order.type.HealthCategory
import java.time.Instant

class SleepHourDialog(private val validator : SleepHourViewValidator, private val viewModel: ChartDetailViewModel) : AbstractHealthDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = DialogSleephourBinding.inflate(inflater, container, false)
        view.title.text = HealthCategory.SLEEP_HOUR.displayName
        initListener(view)
        return view.root
    }

    private fun initListener(view : DialogSleephourBinding) {
        view.date.setThrottleClickListener(1000L) {
            // date pick
            openDateTimePicker(object : DateTimePickerCallback {
                override fun onDateTimeSelected(millisecond: Long) {
                    view.date.text = DateTimeUtil.convertToString(Instant.ofEpochMilli(millisecond))
                }
            })
        }
        view.save.setThrottleClickListener {
            //validator 추가
            when (validator.validate(view)) {
                ValidateStatus.OK -> {
                    viewModel.saveSleepHour(view.date.text.toString(), view.sleepHour.text.toString()) //검증됨
                    dismiss() //요청 후 종료
                }
                ValidateStatus.FIELD_EMPTY -> Toast.makeText(requireContext(), R.string.field_empty, Toast.LENGTH_SHORT).show()
                ValidateStatus.VALUE_ERROR -> Toast.makeText(requireContext(), R.string.value_error, Toast.LENGTH_SHORT).show()
            }

        }
    }
}