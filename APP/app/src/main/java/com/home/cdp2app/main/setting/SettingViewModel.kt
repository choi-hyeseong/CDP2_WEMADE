package com.home.cdp2app.main.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.common.util.livedata.Event
import com.home.cdp2app.user.token.usecase.DeleteAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 설정에서 로그아웃기능 구현위한 VM
 * @param deleteAuthToken 로그아웃시 authToken을 제거하기 위한 유스케이스
 */
@HiltViewModel
class SettingViewModel @Inject constructor(private val deleteAuthToken: DeleteAuthToken) : ViewModel() {

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