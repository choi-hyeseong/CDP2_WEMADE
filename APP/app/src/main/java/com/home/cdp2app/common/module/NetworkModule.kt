package com.home.cdp2app.common.module

import com.home.cdp2app.main.predict.api.PredictAPI
import com.home.cdp2app.user.sign.api.RemoteUserAPI
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Singleton


//hilt network module
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val URL : String = "http://13.124.173.218:8080"

    @Provides
    @Singleton
    fun provideOkHttp() : OkHttpClient {
        // okhttp 제공
        return OkHttpClient.Builder().readTimeout(Duration.ofMinutes(1)).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client : OkHttpClient) : Retrofit {
        // retrofit 제공
        return Retrofit.Builder()
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create()) // sandwich
            .addConverterFactory(GsonConverterFactory.create()) //gson
            .client(client)
            .baseUrl(URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserAPI(retrofit: Retrofit) : RemoteUserAPI {
        return retrofit.create(RemoteUserAPI::class.java)
    }


    @Provides
    @Singleton
    fun providePredictAPI(retrofit: Retrofit) : PredictAPI {
        return retrofit.create(PredictAPI::class.java)
    }

}