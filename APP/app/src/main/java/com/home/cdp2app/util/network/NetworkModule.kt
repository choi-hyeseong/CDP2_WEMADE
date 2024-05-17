package com.home.cdp2app.util.network

import com.home.cdp2app.rest.api.PredictAPI
import com.home.cdp2app.rest.api.RemoteUserAPI
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

//추후 hilt로 옮길 network 모듈
class NetworkModule {

    companion object {
        private val client : OkHttpClient by lazy {
            OkHttpClient.Builder().readTimeout(Duration.ofMinutes(1)).build()
        }

        //https 미지원인데 s 붙이는경우 server error
        val retrofit : Retrofit by lazy {
            Retrofit.Builder()
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl("http://10.0.2.2:8080")
            .build()
        }

        val userApi : RemoteUserAPI by lazy {
            retrofit.create(RemoteUserAPI::class.java)
        }

        val predictAPI : PredictAPI by lazy {
            retrofit.create(PredictAPI::class.java)
        }
    }



}