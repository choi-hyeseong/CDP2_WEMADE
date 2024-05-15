package com.home.cdp2app.view.viewmodel

import android.media.metrics.Event
import androidx.lifecycle.MutableLiveData
import com.home.cdp2app.user.auth.usecase.HasAuthToken
import com.home.cdp2app.user.tutorial.usecase.CheckTutorialCompleted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 초기 MainActivity에서 화면 전환 위한 뷰모델
 *
 * 튜토리얼 미 완료시 -> 튜토리얼 화면 이동
 * AuthToken 없는경우 -> 로그인 화면 이동
 * @property hasAuthToken AuthToken이 있는지 여부를 확인하는 유스케이스
 * @property checkTutorialCompleted 튜토리얼을 완료했는지 여부를 확인하는 유스케이스
 */
class MainViewModel(private val hasAuthToken: HasAuthToken, private val checkTutorialCompleted: CheckTutorialCompleted) {

    //tutorial 여부 확인하는 메소드 observe르 사용
    fun checkTutorialStatus() : MutableLiveData<Boolean> {
        //boolean인 이유는 다회 사용되도 문제가 없는게 어처피 activity 이동하니..
        //매 activity 진입시 체크를 수행해야 하므로, 새로운 livedata를 제공해줘야 함. (설정 - 로그아웃부분)
        val liveData : MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            liveData.postValue(checkTutorialCompleted())
        }
        return liveData
    }

    fun checkAuthToken() : MutableLiveData<Boolean> {
        val liveData : MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            liveData.postValue(hasAuthToken())
        }
        return liveData
    }
}