package com.home.cdp2app.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.home.cdp2app.R
import com.home.cdp2app.databinding.DialogSleephourBinding
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus
import com.home.cdp2app.view.dialog.validator.validate.sleep.SleepHourViewValidator
import com.home.cdp2app.view.viewmodel.ChartDetailViewModel
import java.time.Instant

class SleepHourDialog(private val validator : SleepHourViewValidator, private val viewModel: ChartDetailViewModel) : AbstractHealthDialog() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = DialogSleephourBinding.inflate(inflater, container, false)
        view.title.text = HealthCategory.SLEEP_HOUR.displayName
        initListener(view)
        return view.root
    }

    private fun initListener(view : DialogSleephourBinding) {
        view.date.setOnClickListener {
            // date pick
            openDateTimePicker(object : DateTimePickerCallback {
                override fun onDateTimeSelected(millisecond: Long) {
                    view.date.text = DateTimeUtil.convertToString(Instant.ofEpochMilli(millisecond))
                }
            })
        }
        view.save.setOnClickListener {
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