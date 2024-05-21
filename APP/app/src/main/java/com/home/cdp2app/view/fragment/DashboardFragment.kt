package com.home.cdp2app.view.fragment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.home.cdp2app.R
import com.home.cdp2app.databinding.MainDashboardBinding
import com.home.cdp2app.databinding.MainDashboardItemBinding
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.bloodpressure.repository.HealthConnectBloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.heart.repository.HealthConnectHeartRepository
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.order.repository.PreferenceOrderRepository
import com.home.cdp2app.health.order.usecase.LoadChartOrder
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import com.home.cdp2app.health.sleep.repository.HealthConnectSleepRepository
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.callback.MainPagerCallback
import com.home.cdp2app.view.chart.formatter.DateFormatter
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.view.chart.parser.mapper.SleepHourChartMapper
import com.home.cdp2app.view.viewmodel.dashboard.DashboardViewModel

// dashboard view
class DashboardFragment() : Fragment() {

    private lateinit var adapter : ChartAdapter
    private var callback : MainPagerCallback? = null
    // todo hilt inject
    private val dashboardViewModel : DashboardViewModel by lazy {
        val repository = PreferenceOrderRepository(SharedPreferencesStorage(requireContext()))
        val healthDao = HealthConnectDao(requireContext())
        val bloodRepo = HealthConnectBloodPressureRepository(healthDao, BloodPressureMapper())
        val heartRepo = HealthConnectHeartRepository(healthDao, HeartRateMapper())
        val sleepRepo = HealthConnectSleepRepository(SleepHourMapper(), healthDao)
        DashboardViewModel(
            LoadChartOrder(repository),
            LoadHeartRate(heartRepo),
            LoadBloodPressure(bloodRepo),
            LoadSleepHour(sleepRepo),
            ChartParser(listOf(HeartRateChartMapper(), BloodPressureDiastolicChartMapper(), BloodPressureSystolicChartMapper(), SleepHourChartMapper()))
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = MainDashboardBinding.inflate(inflater)
        initObserver(bind)
        return bind.root
    }

    //observer 초기화
    private fun initObserver(bind : MainDashboardBinding) {
        adapter = ChartAdapter(mutableListOf(), dashboardViewModel, callback).also {
            // empty adapter init (adapter 나중에 지정하니 오류 발생)
            bind.chartRecycler.adapter = it
        }
        //recyclerview observing
        dashboardViewModel.chartLiveData.observe(viewLifecycleOwner) {
            adapter.updateView(it)
        }

        dashboardViewModel.toastLiveData.observe(viewLifecycleOwner) {
            //이벤트가 아직 핸들되지 않았을경우 토스트 발생
            it.getContent()?.let { category ->
                Toast.makeText(requireContext(), getString(R.string.sync_complete, category.displayName), Toast.LENGTH_LONG).show()
            }
        }

    }

    //callback 할당
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as MainPagerCallback?
    }

    //callback 제거
    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    // view에서 표시할 chart 받는 adapter
    class ChartAdapter(var chartList: List<Chart>, private val viewModel: DashboardViewModel, private val callback: MainPagerCallback?) : RecyclerView.Adapter<ChartViewHolder>() {

        fun updateView(chart: List<Chart>) {
            this.chartList = chart
            notifyDataSetChanged() //변경된 row랑 같이 업데이트 하면 좋다는데 이거 판단하기가..
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
            val inflater = LayoutInflater.from(parent.context) //inflater 흭득
            val item: MainDashboardItemBinding = MainDashboardItemBinding.inflate(inflater, parent, false) //parent의 inflater를 이용해서 item view 보여줌
            return ChartViewHolder(item, viewModel, callback)
        }

        override fun getItemCount(): Int {
            return chartList.size
        }

        override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
            holder.bind(chartList[position]) //pos로 접근해서 bind 요청
        }

    }

    //Adapter에서 bind된 layout 받아서 holder에서 가공
    class ChartViewHolder(private val view: MainDashboardItemBinding, private val viewModel: DashboardViewModel, private val callback : MainPagerCallback?) : RecyclerView.ViewHolder(view.root) {

        fun bind(chart: Chart) {
            view.title.text = chart.type.displayName //displayName으로 설정
            view.sync.setOnClickListener {
                //동기화 버튼 로직
                viewModel.requestSync(chart.type)
            }
            view.detail.setOnClickListener {
                callback?.navigateDetail(chart.type)
            }
            applyChart(chart, view.chart) //chart 업데이트
        }

        //chart 내용 적용하는 메소드
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
                    setColor(ColorDrawable(ContextCompat.getColor(context, R.color.main_blue)).color)
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

}

