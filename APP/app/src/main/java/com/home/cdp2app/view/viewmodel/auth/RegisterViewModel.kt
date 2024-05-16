package com.home.cdp2app.view.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.home.cdp2app.rest.type.NetworkStatus
import com.home.cdp2app.user.auth.usecase.RegisterUseCase
import com.home.cdp2app.user.auth.validator.RegisterValidator
import com.home.cdp2app.util.livedata.Event
import com.home.cdp2app.valid.type.ValidateStatus
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerValidator: RegisterValidator, private val registerUseCase: RegisterUseCase) {

    private val LOG_HEADER = "REGISTER_VIEWMODEL"

    val validateLiveData : MutableLiveData<Event<ValidateStatus>> = MutableLiveData() //validate 알림용 라이브데이터
    val registerStatusLiveData : MutableLiveData<Event<NetworkStatus>> = MutableLiveData() //회원가입 여부 확인용 라이브데이터


    fun register(email : String, password : String, nickname : String) {
        //validate
        val validateStatus = registerValidator.validate(email, password, nickname)
        if (validateStatus != ValidateStatus.OK) {
            // validate가 되지 않았을경우 알려주고 리턴
            validateLiveData.postValue(Event(validateStatus))
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            registerUseCase(email, password, nickname).onSuccess {
                registerStatusLiveData.postValue(Event(NetworkStatus.OK)) //성공한경우
            }.onError {
                registerStatusLiveData.postValue(Event(NetworkStatus.fromStatusCode(statusCode))) //BadRequest, Internel Error..
                Log.w(LOG_HEADER, "Encountered Retrofit Error. Status : $statusCode body : ${errorBody?.string()}") //로깅 - 에러
            }.onException {
                registerStatusLiveData.postValue(Event(NetworkStatus.fromException(exception))) //IoException 등등..
                Log.w(LOG_HEADER, "Encountered Retrofit Exception. Exception : ${exception.javaClass.simpleName}") //로깅 - 예외
            }
        }
    }
}