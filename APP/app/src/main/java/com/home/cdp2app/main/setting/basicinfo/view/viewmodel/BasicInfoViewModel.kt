package com.home.cdp2app.main.setting.basicinfo.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.common.util.livedata.Event
import com.home.cdp2app.main.setting.basicinfo.entity.BasicInfo
import com.home.cdp2app.main.setting.basicinfo.type.Gender
import com.home.cdp2app.main.setting.basicinfo.usecase.LoadBasicInfo
import com.home.cdp2app.main.setting.basicinfo.usecase.SaveBasicInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BasicInfoFragment에 사용되는 VM
 * @property loadBasicInfo BasicInfo를 불러오는데 사용됩니다.
 * @property saveBasicInfo BasicInfo를 저장하는데 사용됩니다.
 */
@HiltViewModel
class BasicInfoViewModel @Inject constructor(private val loadBasicInfo: LoadBasicInfo, private val saveBasicInfo: SaveBasicInfo) : ViewModel() {

    //저장 완료여부 확인시켜주기 위한 Event Boolean LiveData. 1회성으로만 작동함. (enum 안쓴 이유는 1가지 메시지만 나타내면 되는데 굳이 enum 구현 까지 필요한가.. 싶음)
    val saveLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()

    //basic info 저장용 LiveData
    val basicInfoLiveData: MutableLiveData<BasicInfo> by lazy {
        MutableLiveData<BasicInfo>().also { loadInfo() }
    }

    private fun loadInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val info = loadBasicInfo(true)
            basicInfoLiveData.postValue(info)
        }
    }

    // BasicInfo 저장시 사용되는 함수. 저장 버튼 누를때 호출됨
    fun saveBasicInfo(height: Int, weight: Int, isSmoking: Boolean, age : Int, gender: Gender) {
        CoroutineScope(Dispatchers.IO).launch {
            saveBasicInfo(BasicInfo(height.toDouble(), weight.toDouble(), age, gender, isSmoking))
            saveLiveData.postValue(Event(true)) //저장된경우 true 전달
        }
    }

}