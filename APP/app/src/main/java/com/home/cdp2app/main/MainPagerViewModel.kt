package com.home.cdp2app.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.main.setting.basicinfo.usecase.HasBasicInfo
import com.home.cdp2app.common.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 메인 ViewPager를 보여주기전, 기본정보 (BasicInfo)가 저장되어 있는지 여부 확인
 */
class MainPagerViewModel(private val hasBasicInfo: HasBasicInfo) : ViewModel() {

    fun checkHaveBasicInfo() : LiveData<Event<Boolean>> {
        val liveData : MutableLiveData<Event<Boolean>> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            liveData.postValue(Event(hasBasicInfo()))
        }
        return liveData
    }
}