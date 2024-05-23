package com.home.cdp2app.common.network.type

import com.skydoves.sandwich.StatusCode
import java.io.IOException

/**
 * Retrofit 요청의 응답으로 받은 HttpStatus를 나타내는 Enum입니다.
 * @property OK 요청이 성공적으로 전달되었습니다. : HttpStatus_OK
 * @property CONNECTION_ERROR IOException, TimeOut등에 해당되며, 연결에 이상이 생긴경우 반환합니다.
 * @property BAD_REQUEST 요청이 잘못되었습니다.
 * @property UNAUTHORIZED 인증 (JWT)가 필요합니다. 인증이 만료됐을경우에도 발생합니다.
 * @property INTERNAL_ERROR 서버 내부에서 문제가 생긴경우 발생합니다. (타임아웃
 * @property OTHER 위 사례에 해당되지 않는경우 발생합니다. (리다이렉트, NO_Content등등 다 포함)
 */
enum class NetworkStatus {
    OK, CONNECTION_ERROR, BAD_REQUEST, UNAUTHORIZED, INTERNAL_ERROR, OTHER;

    companion object {

        /**
         * StatusCode를 NetworkStatus Enum으로 변경합니다.
         * @param code 변경할 StatusCode입니다.
         * @return 변환된 Enum입니다.
         */
        fun fromStatusCode(code : StatusCode) : NetworkStatus {
            return when (code) {
                StatusCode.OK -> OK
                StatusCode.Unauthorized -> UNAUTHORIZED
                StatusCode.BadRequest -> BAD_REQUEST
                StatusCode.InternalServerError -> INTERNAL_ERROR
                StatusCode.RequestTimeout -> CONNECTION_ERROR
                else -> OTHER
            }
        }

        /**
         * Exception을 NetworkStatus로 변환함
         * @param e 예외입니다.
         * @return 변환된 enum입니다.
         */
        fun fromException(e : Throwable) : NetworkStatus {
            return when (e) {
                // 연결 과정에서 발생하는 문제는 대부분 IOException -> 그 외에는 OTHER로 처리
                is IOException -> CONNECTION_ERROR
                else -> OTHER
            }
        }
    }
}