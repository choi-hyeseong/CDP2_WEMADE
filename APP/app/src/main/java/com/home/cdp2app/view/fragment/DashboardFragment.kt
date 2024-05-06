package com.home.cdp2app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.home.cdp2app.R
import com.home.cdp2app.databinding.ChartRecyclerBinding
import com.home.cdp2app.databinding.ChartRecyclerItemBinding
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.bloodpressure.repository.HealthConnectBloodPressureRepository
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.parser.ChartParser
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.view.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.view.viewmodel.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

// dashboard view
// TODO Detail
class DashboardFragment(private val dashboardViewModel: DashboardViewModel) : Fragment() {

    private var adapter : ChartAdapter? = null //추후 초기화 될 adapter. observe시 값 갱신에 사용됨

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = ChartRecyclerBinding.inflate(inflater)
        initObserver(bind)
        return bind.root
    }

    //observer 초기화
    private fun initObserver(bind : ChartRecyclerBinding) {
        //recyclerview observing
        dashboardViewModel.chartList.observe(viewLifecycleOwner) {
            if (adapter != null) //adapter가 이미 지정되어 있다면
                adapter!!.updateView(it) //notnull이므로 !!
            else {
                //adapter 지정
                adapter = ChartAdapter(it, dashboardViewModel)
                bind.chartRecycler.adapter = adapter
            }
        }

        dashboardViewModel.toastLiveData.observe(viewLifecycleOwner) {
            //이벤트가 아직 핸들되지 않았을경우 토스트 발생
            it.getContent()?.let { category ->
                Toast.makeText(requireContext(), getString(R.string.sync_complete, category.displayName), Toast.LENGTH_LONG).show()
            }
        }
        dashboardViewModel.loadAllChartData() //차트 데이터 모두 불러오기

    }
}

// view에서 표시할 chart 받는 adapter
class ChartAdapter(var chartList: List<Chart>, private val viewModel: DashboardViewModel) : RecyclerView.Adapter<ChartViewHolder>() {

    fun updateView(chart: List<Chart>) {
        this.chartList = chart
        notifyDataSetChanged() //변경된 row랑 같이 업데이트 하면 좋다는데 이거 판단하기가..
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val inflater = LayoutInflater.from(parent.context) //inflater 흭득
        val item: ChartRecyclerItemBinding = ChartRecyclerItemBinding.inflate(inflater, parent, false) //parent의 inflater를 이용해서 item view 보여줌
        return ChartViewHolder(item, viewModel)
    }

    override fun getItemCount(): Int {
        return chartList.size
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(chartList[position]) //pos로 접근해서 bind 요청
    }

}

//Adapter에서 bind된 layout 받아서 holder에서 가공
class ChartViewHolder(private val view: ChartRecyclerItemBinding, private val viewModel: DashboardViewModel) : RecyclerView.ViewHolder(view.root) {

    fun bind(chart: Chart) {
        view.title.text = chart.type.displayName //displayName으로 설정
        view.sync.setOnClickListener {
            //동기화 버튼 로직
            viewModel.requestSync(chart.type)
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

            xAxis.valueFormatter = Formatter(chartData.map { it.time }) //시간만 따로 추출. Entry 생성시 인덱스로 넣었기 때문에 매핑됨

            //yAxis 설정
            axisLeft.setDrawAxisLine(false)
            axisLeft.textColor = R.color.black

            axisRight.isEnabled = false
            data = BarData(BarDataSet(entries, chartEntity.type.displayName).apply {
                setColor(R.color.main_blue, 125)
                valueTextSize = 12.0f
            }) //bar color
            setVisibleXRangeMaximum(5f) //dateset 넣고 visible 설정가능
        }
        viewChart.data.notifyDataChanged()
        viewChart.notifyDataSetChanged() //data 갱신
        viewChart.invalidate() //view 갱신

    }

}

//instant를 float으로 변경하는게 아닌, Date를 따로 만들어서 index와 비교해서 보여주는 방식으로
class Formatter(val date: List<Instant>) : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return SimpleDateFormat("MM-dd HH:mm", Locale.KOREA).format(Date.from(date[value.toInt()]))
    }
}

