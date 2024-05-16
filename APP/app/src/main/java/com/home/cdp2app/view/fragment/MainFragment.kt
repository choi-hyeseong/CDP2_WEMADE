package com.home.cdp2app.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.home.cdp2app.R
import com.home.cdp2app.databinding.MainPredictBinding
import com.ramijemli.percentagechartview.callback.AdaptiveColorProvider
import java.util.concurrent.ThreadLocalRandom

class MainFragment : Fragment() {

    // TODO 현재 view null renderer문제 있음 확인할것.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = MainPredictBinding.inflate(inflater, container, false)
        initView(bind)
        bind.predict.setOnClickListener {
            bind.percentageChartView.setProgress(ThreadLocalRandom.current().nextDouble(0.0, 100.0).toFloat(), true)
        }
        return bind.root

    }

    private fun initView(bind : MainPredictBinding) {
        //뷰 색상 지정등등
        bind.percentageChartView.let {
            it.textSize = 70f
            it.setAdaptiveColorProvider(ChartColorProvider(requireContext()))
            it.apply()
        }
    }

    class ChartColorProvider(private val context : Context) : AdaptiveColorProvider {
        override fun provideTextColor(progress: Float): Int {
            return ContextCompat.getColor(context, R.color.black)
        }

        override fun provideProgressColor(progress: Float): Int {
            return if (progress <= 25) Color.GREEN //0~25%
            else if (progress <= 50) Color.YELLOW //25~50%
            else if (progress <= 75) ContextCompat.getColor(context, R.color.vermilion)
            else ContextCompat.getColor(context, R.color.scarlet)
        }

        override fun provideBackgroundColor(progress: Float): Int {
            return ColorUtils.blendARGB(provideProgressColor(progress), Color.BLACK, .8f)
        }

        override fun provideBackgroundBarColor(progress: Float): Int {
            return ContextCompat.getColor(context, R.color.light_grey)
        }
    }
}