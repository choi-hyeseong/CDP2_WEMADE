package com.home.cdp2app

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

//Retrofit test용 IP API 호출
interface TestAPIService {

    // ip 응답 정보를 받아오는 end-point
    @GET("./")
    suspend fun getIPAddress() : ApiResponse<TestAPIResponse>
}