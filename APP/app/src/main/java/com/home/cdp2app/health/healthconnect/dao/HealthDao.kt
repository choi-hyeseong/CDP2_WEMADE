package com.home.cdp2app.health.healthconnect.dao

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import java.time.Instant
import kotlin.reflect.KClass

/**
 * HealthConnect와의 데이터 상호작용을 하는 Dao.
 * HealthConnect의 TimeRangeFilter를 되도록 안쓰기 위해 Between, Before, After 3가지의 Read 메소드를 생성.
 */
interface HealthDao {

    /**
     * Health Connect에 Record를 주입하는 메소드. Record 객체 내부에 시간이 지정되어 있어 날짜 지정이 가능합니다.
     * @param record 저장할 레코드 입니다.
     */
    //todo multiple record insert support (1:1로 매핑되는 혈압 등등은 List<Record>형태로 가공됨)
    suspend fun insertRecord(record: Record)

    /**
     * Health Connect에서 특정 기간의 Record를 읽어옵니다.
     * @param recordClass 읽어올 클래스 타입입니다.
     * @param start 탐색할 시작 날짜입니다.
     * @param end 탐색 종료할 날짜입니다.
     */
    suspend fun <T : Record> readRecordBetween(recordClass : KClass<T>, start : Instant, end : Instant): List<T>

    /**
     * Health Connect에서 특정 날짜 이전의 Record를 읽어옵니다.
     * @param recordClass 읽어올 클래스 타입입니다.
     * @param date 대상 날짜입니다.
     */
    suspend fun <T : Record> readRecordBefore(recordClass : KClass<T>, date : Instant) : List<T>

    /**
     * Health Connect에서 특정 날짜 이후의 Record를 읽어옵니다.
     * @param recordClass 읽어올 클래스 타입입니다.
     * @param date 대상 날짜입니다.
     */
    suspend fun <T : Record> readRecordAfter(recordClass : KClass<T>, date : Instant) : List<T>
}