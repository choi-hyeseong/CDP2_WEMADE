package com.home.cdp2app.tutorial.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.tutorial.usecase.SaveTutorialCompleted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 튜토리얼 완료 여부 저장하기 위한 ViewModel
 */
@HiltViewModel
class TutorialViewModel @Inject constructor(private val saveTutorialCompleted: SaveTutorialCompleted) : ViewModel() {

    fun saveTutorialEnded() : MutableLiveData<Boolean> {
        val liveData : MutableLiveData<Boolean> = MutableLiveData()
        CoroutineScope(Dispatchers.IO).launch {
            saveTutorialCompleted()
            liveData.postValue(true) //저장 완료 post
        }
        return liveData
    }
}