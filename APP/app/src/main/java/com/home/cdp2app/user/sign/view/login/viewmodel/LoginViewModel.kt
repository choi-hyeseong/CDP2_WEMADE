package com.home.cdp2app.user.sign.view.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.cdp2app.common.network.type.NetworkStatus
import com.home.cdp2app.user.sign.usecase.LoginUseCase
import com.home.cdp2app.user.token.usecase.SaveAuthToken
import com.home.cdp2app.user.sign.validator.LoginValidator
import com.home.cdp2app.common.util.livedata.Event
import com.home.cdp2app.common.valid.type.ValidateStatus
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginValidator: LoginValidator, private val loginUseCase: LoginUseCase, private val saveAuthToken: SaveAuthToken) : ViewModel() {

    private val LOG_HEADER = "LOGIN_VIEWMODEL"

    val validateLiveData : MutableLiveData<Event<ValidateStatus>> = MutableLiveData() //validate 알림용 라이브데이터
    val loginStatusLiveData : MutableLiveData<Event<NetworkStatus>> = MutableLiveData() //로그인 여부 확인용 라이브데이터


    fun login(email : String, password : String) {
        //validate
        val validateStatus = loginValidator.validate(email, password)
        if (validateStatus != ValidateStatus.OK) {
            // validate가 되지 않았을경우 알려주고 리턴
            validateLiveData.postValue(Event(validateStatus))
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            loginUseCase(email, password).suspendOnSuccess {
                //suspend method 제공
                saveAuthToken(data.toEntity()) //저장된 dto를 entity로 변환하고 저장
                loginStatusLiveData.postValue(Event(NetworkStatus.OK)) //성공한경우 ok 리턴
            }.onError {
                loginStatusLiveData.postValue(Event(NetworkStatus.fromStatusCode(statusCode))) //BadRequest, Internel Error..
                Log.w(LOG_HEADER, "Encountered Retrofit Error. Status : $statusCode body : ${errorBody?.string()}") //로깅 - 에러
            }.onException {
                loginStatusLiveData.postValue(Event(NetworkStatus.fromException(exception))) //IoException 등등..
                Log.w(LOG_HEADER, "Encountered Retrofit Exception. Exception : ${exception.javaClass.simpleName}") //로깅 - 예외
            }
        }
    }

}