package com.home.cdp2app.rest.type

import com.google.gson.JsonSyntaxException
import com.skydoves.sandwich.StatusCode
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkStatusTest {

    @Test
    fun TEST_OK() {
        assertEquals(NetworkStatus.OK, NetworkStatus.fromStatusCode(StatusCode.OK))
    }

    @Test
    fun TEST_UNAUTHROIZED() {
        assertEquals(NetworkStatus.UNAUTHORIZED, NetworkStatus.fromStatusCode(StatusCode.Unauthorized))
    }

    @Test
    fun TEST_BAD_REQUEST() {
        assertEquals(NetworkStatus.BAD_REQUEST, NetworkStatus.fromStatusCode(StatusCode.BadRequest))
    }

    @Test
    fun TEST_INTERNAL_ERROR() {
        assertEquals(NetworkStatus.INTERNAL_ERROR, NetworkStatus.fromStatusCode(StatusCode.InternalServerError))
    }

    @Test
    fun TEST_CONNECTION_ERROR() {
        assertEquals(NetworkStatus.CONNECTION_ERROR, NetworkStatus.fromStatusCode(StatusCode.RequestTimeout))
    }

    @Test
    fun TEST_OTHER() {
        assertEquals(NetworkStatus.OTHER, NetworkStatus.fromStatusCode(StatusCode.AlreadyReported))
        assertEquals(NetworkStatus.OTHER, NetworkStatus.fromStatusCode(StatusCode.BadGateway))
    }

    @Test
    fun TEST_CONNECTION_ERROR_FROM_EXCEPTION() {
        assertEquals(NetworkStatus.CONNECTION_ERROR, NetworkStatus.fromException(IOException()))
        assertEquals(NetworkStatus.CONNECTION_ERROR, NetworkStatus.fromException(SocketTimeoutException())) //소켓 타임아웃
        assertEquals(NetworkStatus.CONNECTION_ERROR, NetworkStatus.fromException(UnknownHostException())) //호스트 찾을 수 없음
    }

    @Test
    fun TEST_OTHER_EXCEPTION() {
        assertEquals(NetworkStatus.OTHER, NetworkStatus.fromException(JsonSyntaxException("")))
    }


}