package com.home.cdp2app.view.mapper

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.home.cdp2app.R
import com.home.cdp2app.databinding.MainPredictBinding
import com.home.cdp2app.predict.entity.PredictResult

class PredictViewMapper(private val context : Context, private val binding : MainPredictBinding) {

    fun notify(result : PredictResult) {
        val percent = result.percent
        binding.percentageChartView.setProgress(result.percent.toFloat(), true) //progress 변경
        if (percent <= 25)
            onPredictGreat()
        else if (percent <= 50)
            onPredictCaution()
        else if (percent <= 75)
            onPredictWarn()
        else
            onPredictSerious()
    }

    private fun onPredictGreat() {
        //굿 - 초록색 0 ~ 25
        binding.apply {
            title.let {
                it.text = context.getString(R.string.predict_good_title)
                it.setTextColor(Color.GREEN)
            }
            subText.text = context.getString(R.string.predict_good_subtitle)
        }
    }

    private fun onPredictCaution() {
        //경고 - 노란색, 25 ~ 50
        binding.apply {
            title.let {
                it.text = context.getString(R.string.predict_caution_title)
                it.setTextColor(ContextCompat.getColor(context, R.color.yellow))
            }
            subText.text = context.getString(R.string.predict_caution_subtitle)
        }
    }

    private fun onPredictWarn() {
        //위험 - 옅은 빨강 50 ~ 75
        binding.apply {
            title.let {
                it.text = context.getString(R.string.predict_warn_title)
                it.setTextColor(ContextCompat.getColor(context, R.color.vermilion))
            }
            subText.text = context.getString(R.string.predict_warn_subtitle)
        }
    }

    private fun onPredictSerious() {
        //심각 - 빨간색 75 ~ 100
        binding.apply {
            title.let {
                it.text = context.getString(R.string.predict_serious_title)
                it.setTextColor(ContextCompat.getColor(context, R.color.scarlet))
            }
            subText.text = context.getString(R.string.predict_serious_subtitle)
        }
    }


}