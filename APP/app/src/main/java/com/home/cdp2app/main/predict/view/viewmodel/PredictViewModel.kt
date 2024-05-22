package com.home.cdp2app.main.predict.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.main.predict.entity.PredictResult
import com.home.cdp2app.main.predict.usecase.GetCachePredictResult
import com.home.cdp2app.main.predict.usecase.PredictUseCase
import com.home.cdp2app.main.predict.usecase.SaveCachePredictResult
import com.home.cdp2app.rest.type.NetworkStatus
import com.home.cdp2app.user.token.usecase.DeleteAuthToken
import com.home.cdp2app.util.livedata.Event
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PredictViewModel(private val deleteAuthToken: DeleteAuthToken, private val predictUseCase: PredictUseCase, private val saveCachePredictResult: SaveCachePredictResult, private val getCachePredictResult: GetCachePredictResult) : ViewModel() {

    private var job : Job? = null //viewpager 움직임등으로 인해 요청 중단 필요시 job
    private val HEADER : String = "PREDICT_VIEWMODEL"
    //예측 결과를 나타내는 livedata
    val predictLiveData : MutableLiveData<PredictResult> by lazy {
        MutableLiveData<PredictResult>().also { loadFromCache() }
    }
    //network 상태 알려주는 Livedata
    val networkStatus : MutableLiveData<Event<NetworkStatus>> = MutableLiveData()


    // 캐시에서 이전 결과값 불러오기
    private fun loadFromCache() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = getCachePredictResult()
            if (result != null)
                predictLiveData.postValue(result)
        }
    }

    fun requestPredict(exercise : Boolean) {
        Log.i(HEADER, "EXERCISE : $exercise")
        job = CoroutineScope(Dispatchers.IO).launch {
            predictUseCase().suspendOnSuccess {
                //suspend 함수를 위한 suspend 수행
                networkStatus.postValue(Event(NetworkStatus.OK)) //network status를 먼저 보내서 로딩창 없애기

                val result = data.toEntity() //entity로 변환하기
                saveCachePredictResult(result) //캐시 저장 - 재실행시
                predictLiveData.postValue(result) //predict view 표시
            }.suspendOnError {
                if (statusCode == StatusCode.Unauthorized)
                    deleteAuthToken()  //Unauthorized인경우 저장된 토큰 제거함. - View에서 로그인 재 유도 위함
                networkStatus.postValue(Event(NetworkStatus.fromStatusCode(statusCode)))
                Log.w(HEADER, "Encountered Retrofit Error. Status : $statusCode body : ${errorBody?.string()}") //로깅 - 에러
            }.onException {
                networkStatus.postValue(Event(NetworkStatus.fromException(exception)))
                Log.w(HEADER, "Encountered Retrofit Exception. Exception : ${exception.javaClass.simpleName}") //로깅 - 예외
            }
        }
    }

    fun cancel() {
        // viewpager 전환시등 요청중인 작업 취소
        job?.cancel()
    }
}