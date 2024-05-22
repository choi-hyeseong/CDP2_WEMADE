package com.home.cdp2app.main.setting.order.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.main.setting.order.entity.ChartOrder
import com.home.cdp2app.main.setting.order.type.HealthCategory
import com.home.cdp2app.main.setting.order.usecase.LoadChartOrder
import com.home.cdp2app.main.setting.order.usecase.SaveChartOrder
import com.home.cdp2app.common.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedHashSet

// 대시보드 순서 정보 저장하고 가져오는 뷰모델
// 왜 liveData에 값을 저장하는가 - 임시 데이터라도 뷰가 데이터를 갖고 있으면 화면 회전시 손실할 가능성이 있음.
// 추가로, MVVM 패턴상 뷰는 로직을 최대한 모델과 관련된 로직을 처리해선 안됨. 뷰모델에게 위임하는게 좋음
// 개선할 수 있다면 LiveData value에 직접 접근하는게 아니라, 차라리 필드를 하나 만들어놓고 접근하는게 좋을거 같음.

// 20:31 개선
class DashboardOrderViewModel(private val loadChartOrder: LoadChartOrder, private val saveChartOrder: SaveChartOrder) : ViewModel() {

    // orderLiveData 접근시 차트 순서 로드
    val orderLivedata : MutableLiveData<ChartOrder> by lazy {
        MutableLiveData<ChartOrder>().also { loadOrder() }
    }
    val saveLiveData : MutableLiveData<Event<Boolean>> = MutableLiveData() //저장여부 확인용 livedata
    // load로 가져온순간 저장된 값과는 다른 값이기 때문에 깊은 복사임
    private lateinit var chartOrder: ChartOrder //vm에서 order를 가지고 있음 (state, 뷰에서 처리하지 않기 위함)

    // order가 수행되는 시점. lazy로 접근하여 생성자 init보다는 나은것으로 보임
    private fun loadOrder() {
        CoroutineScope(Dispatchers.IO).launch {
            val latestOrder = loadChartOrder()
            chartOrder = latestOrder //값 init
            orderLivedata.postValue(latestOrder)
        }
    }

    fun update(orders : LinkedHashSet<HealthCategory>) {
        chartOrder.update(orders)
    }

    fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            // orderLiveData가 초기화 되어 있지 않음, enum이 일부 빠져있음 등의 에러를 통합적으로 catching함
            val isSuccess = kotlin.runCatching {
                saveChartOrder(chartOrder)
            }.isSuccess
            saveLiveData.postValue(Event(isSuccess))

        }

    }
}