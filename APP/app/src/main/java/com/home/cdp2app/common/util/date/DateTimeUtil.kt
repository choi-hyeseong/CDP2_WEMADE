package com.home.cdp2app.common.util.date

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * 날짜 - 문자열 변환 전용 유틸. 현재 너무 각자 formatter를 생성하고 따로 놀아서 합쳐서 관리하기
 */
class DateTimeUtil {

    companion object {
        // public으로 사용할 수 있는 formatter를 제공
        val dateFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault()) //spinner에 사용될 날짜 formatter

        /**
         * nullable한 string을 Instant로 변경합니다.
         * @param str nullable한 문자열입니다.
         * @return nullable한 파싱된 Instant로 반환합니다. str이 null이거나 파싱 실패시 null을 반환합니다
         */
        fun convertToDate(str : String?) : Instant? {
            return kotlin.runCatching {
                LocalDateTime.parse(str, dateFormatter).toInstant(ZonedDateTime.now().offset)
            }.getOrNull()
        }

        /**
         * Instant를 문자열로 변경합니다.
         * @param date nullable한 Instant입니다.
         * @return nullable한 문자열입니다. 문자열의 포맷은 dateFormatter를 따릅니다.
         */
        fun convertToString(date : Instant?) : String? {
            return kotlin.runCatching {
                dateFormatter.format(date).toString()
            }.getOrNull()
        }
    }
}