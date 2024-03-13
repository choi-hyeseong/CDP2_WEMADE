package com.home.cdp2app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebComponent {

    companion object {

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder().build()
        }
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://api.ip.pe.kr/json/")
                .build()
        }

        fun getTestService() : TestAPIService {
           return retrofit.create(TestAPIService::class.java)
        }
    }
}