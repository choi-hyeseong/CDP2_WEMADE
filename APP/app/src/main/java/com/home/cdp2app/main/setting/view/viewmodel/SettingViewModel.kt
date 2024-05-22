package com.home.cdp2app.main.setting.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.user.token.usecase.DeleteAuthToken
import com.home.cdp2app.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 설정에서 로그아웃기능 구현위한 VM
 * @param deleteAuthToken 로그아웃시 authToken을 제거하기 위한 유스케이스
 */
class SettingViewModel(private val deleteAuthToken: DeleteAuthToken) : ViewModel() {

    // 로그아웃을 위한 메소드. authToken 제거 후 Event로 알려주면 이동
    fun signOut() : MutableLiveData<Event<Boolean>> {
        // 로그아웃을 위한 일회성 livedata
        val liveData : MutableLiveData<Event<Boolean>> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            deleteAuthToken()
            liveData.postValue(Event(true))
        }
        return liveData
    }

}