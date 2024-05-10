package com.home.cdp2app

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.home.cdp2app.databinding.MainDashboardDetailBinding
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.bloodpressure.repository.HealthConnectBloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.SaveBloodPressure
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.heart.repository.HealthConnectHeartRepository
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.heart.usecase.SaveHeartRate
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import com.home.cdp2app.health.sleep.repository.HealthConnectSleepRepository
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.health.sleep.usecase.SaveSleepHour
import com.home.cdp2app.util.date.DateTimeUtil
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.formatter.DateFormatter
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.view.dialog.factory.DialogFactory
import com.home.cdp2app.view.dialog.validator.type.ValidateStatus
import com.home.cdp2app.view.dialog.validator.validate.blood.BloodPressureValidator
import com.home.cdp2app.view.dialog.validator.validate.blood.BloodPressureViewValidator
import com.home.cdp2app.view.dialog.validator.validate.heart.HeartRateValidator
import com.home.cdp2app.view.dialog.validator.validate.heart.HeartRateViewValidator
import com.home.cdp2app.view.dialog.validator.validate.sleep.SleepHourValidator
import com.home.cdp2app.view.dialog.validator.validate.sleep.SleepHourViewValidator
import com.home.cdp2app.view.viewmodel.ChartDetailViewModel

//해당 액티비티 실행시 Intent에 DETAIL_PARAM값과 함께 HealthCategory enum을 제공해야 합니다.
class ChartDetailActivity : AppCompatActivity() {

    companion object {
        val DETAIL_PARAM: String = "DETAIL_ENUM" //파라미터용 key
    }

    private val heartRateValidator = HeartRateValidator()
    private val sleepHourValidator = SleepHourValidator()
    private val bloodPressureValidator = BloodPressureValidator()

    // todo hilt init
    private val viewModel: ChartDetailViewModel by lazy {
        val healthDao = HealthConnectDao(applicationContext)
        val bloodRepo = HealthConnectBloodPressureRepository(healthDao, BloodPressureMapper())
        val heartRepo = HealthConnectHeartRepository(healthDao, HeartRateMapper())
        val sleepRepo = HealthConnectSleepRepository(SleepHourMapper(), healthDao)
        ChartDetailViewModel(LoadHeartRate(heartRepo), LoadBloodPressure(bloodRepo), LoadSleepHour(sleepRepo),
            SaveHeartRate(heartRepo),
            SaveBloodPressure(bloodRepo),
            SaveSleepHour(sleepRepo),
            heartRateValidator,
            sleepHourValidator,
            bloodPressureValidator,
            ChartParser(listOf(HeartRateChartMapper(),
                BloodPressureDiastolicChartMapper(),
                BloodPressureSystolicChartMapper(),
                SleepHourChartMapper()))
        )
    }

    private val factory: DialogFactory by lazy {
        DialogFactory(HeartRateViewValidator(heartRateValidator), BloodPressureViewValidator(bloodPressureValidator), SleepHourViewValidator(sleepHourValidator))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = MainDashboardDetailBinding.inflate(layoutInflater)
        initParam(intent, bind)
        initObserver(bind)
        setContentView(bind.root)
    }

    private fun initParam(intent: Intent, bind: MainDashboardDetailBinding) {
        //파라미터 파싱
        val param = intent.getStringExtra(DETAIL_PARAM)
        val category = HealthCategory.fromString(param) //nullable한 param 파싱
        if (category == null) {
            // 카테고리 미 제공시 finish로 꺼버림
            Toast.makeText(this, R.string.chart_not_provide, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        else {
            // 차트 로드 요청
            viewModel.loadChart(category)
            bind.title.text = category.displayName
            bind.sync.setOnClickListener {
                viewModel.loadChart(category)
            }

            // dialog
            bind.insertButton.setOnClickListener {
                factory.provide(category, viewModel).show(supportFragmentManager, "DIALOG")
            }
        }
    }

    private fun initObserver(view: MainDashboardDetailBinding) {
        // 차트 갱신 관측
        viewModel.chartLiveData.observe(this) {
            applyChart(it, view.chart)
            view.autoCompleteTextView.apply {
                setAdapter(ArrayAdapter(this@ChartDetailActivity, R.layout.main_dashboard_detail_spinner, it.chartData.map { chartData -> DateTimeUtil.convertToString(chartData.time) }))
                setDropDownBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@ChartDetailActivity, R.color.white)))
                setOnItemClickListener { _, _, position, _ ->
                    view.chart.moveViewToX(position.toFloat()) //차트 뷰 이동
                }
            }
        }
        // 저장 여부
        viewModel.saveLiveData.observe(this) { event ->
            event.getContent()?.let {
                when (it) {
                    ValidateStatus.OK -> Toast.makeText(this, R.string.dialog_save_complete, Toast.LENGTH_LONG).show()
                    ValidateStatus.FIELD_EMPTY -> Toast.makeText(this, R.string.field_empty, Toast.LENGTH_LONG).show()
                    ValidateStatus.VALUE_ERROR -> Toast.makeText(this, R.string.value_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //차트 생성
    private fun applyChart(chartEntity: Chart, viewChart: BarChart) {
        if (chartEntity.chartData.isEmpty()) //빈 데이터의경우 return
            return

        val chartData = chartEntity.chartData
        val entries = chartData.mapIndexed { index, value -> BarEntry(index.toFloat(), value.data.toFloat()) }.toMutableList()
        viewChart.apply {
            //기본 bar chart 설정
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawBorders(false)
            description = Description().apply { isEnabled = false }
            animateY(1000)
            animateX(1000)


            //xAxis 설정
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.textColor = R.color.black
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.labelCount = 7

            xAxis.valueFormatter = DateFormatter(chartData.map { it.time }) //시간만 따로 추출. Entry 생성시 인덱스로 넣었기 때문에 매핑됨

            //yAxis 설정
            axisLeft.setDrawAxisLine(false)
            axisLeft.textColor = R.color.black

            axisRight.isEnabled = false
            data = BarData(BarDataSet(entries, chartEntity.type.displayName).apply {
                setColor(ColorDrawable(ContextCompat.getColor(this@ChartDetailActivity, R.color.main_blue)).color, 125)
                valueTextSize = 12.0f
            }) //bar color
            setVisibleXRangeMinimum(5f)
            setVisibleXRangeMaximum(5f) //dateset 넣고 visible 설정가능
        }
        viewChart.data.notifyDataChanged()
        viewChart.notifyDataSetChanged() //data 갱신
        viewChart.invalidate() //view 갱신

    }

}