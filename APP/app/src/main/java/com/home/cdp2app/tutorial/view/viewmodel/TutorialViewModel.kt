package com.home.cdp2app.tutorial.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.tutorial.usecase.SaveTutorialCompleted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 튜토리얼 완료 여부 저장하기 위한 ViewModel
 */
class TutorialViewModel(private val saveTutorialCompleted: SaveTutorialCompleted) : ViewModel() {

    fun saveTutorialEnded() : MutableLiveData<Boolean> {
        val liveData : MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            saveTutorialCompleted()
            liveData.postValue(true) //저장 완료 post
        }
        return liveData
    }
}