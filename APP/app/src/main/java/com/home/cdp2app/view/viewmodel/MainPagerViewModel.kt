package com.home.cdp2app.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.home.cdp2app.health.basic.usecase.HasBasicInfo
import com.home.cdp2app.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 메인 ViewPager를 보여주기전, 기본정보 (BasicInfo)가 저장되어 있는지 여부 확인
 */
class MainPagerViewModel(private val hasBasicInfo: HasBasicInfo) {

    fun checkHaveBasicInfo() : LiveData<Event<Boolean>> {
        val liveData : MutableLiveData<Event<Boolean>> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            liveData.postValue(Event(hasBasicInfo()))
        }
        return liveData
    }
}