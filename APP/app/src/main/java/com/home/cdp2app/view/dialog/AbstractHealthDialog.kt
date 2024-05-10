package com.home.cdp2app.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.home.cdp2app.R
import com.home.cdp2app.util.date.DateTimeUtil
import java.time.Instant
import java.util.Calendar
import java.util.Date

/**
 * Dialog에서 필요한 몇가지 메소드 모아놓은곳
 */
abstract class AbstractHealthDialog : DialogFragment() {

    // dialog 배경 하얗게
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { it.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.white))) }
    }

    //datetime picker open
    fun openDateTimePicker(callback : DateTimePickerCallback) {
        val picker = MaterialDatePicker.Builder.datePicker().setTitleText(requireContext().getString(R.string.select_date_dialog)).build()
        picker.addOnPositiveButtonClickListener { millis ->
            //time picker
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(12).setMinute(0).setTitleText(R.string.select_time_dialog).build()
            timePicker.addOnPositiveButtonClickListener {
                val date = Date(millis)
                val calender = Calendar.getInstance().apply {
                    time = date
                }
                calender.set(Calendar.HOUR, timePicker.hour)
                calender.set(Calendar.MINUTE, timePicker.minute)
                callback.onDateTimeSelected(calender.timeInMillis) //callback 호출
            }
            timePicker.show(parentFragmentManager, "DIALOG")

        }
        picker.show(parentFragmentManager, "DIALOG")
    }
}

interface DateTimePickerCallback {
    fun onDateTimeSelected(millisecond : Long) {

    }
}