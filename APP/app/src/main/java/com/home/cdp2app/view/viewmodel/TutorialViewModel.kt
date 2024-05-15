package com.home.cdp2app.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.home.cdp2app.user.tutorial.usecase.SaveTutorialCompleted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 튜토리얼 완료 여부 저장하기 위한 ViewModel
 */
class TutorialViewModel(private val saveTutorialCompleted: SaveTutorialCompleted) {

    fun saveTutorialEnded() : MutableLiveData<Boolean> {
        val liveData : MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            saveTutorialCompleted()
            liveData.postValue(true) //저장 완료 post
        }
        return liveData
    }
}