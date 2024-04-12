package com.home.cdp2app.health.healthconnect.mapper

import androidx.health.connect.client.records.Record

/**
 * HealthConnect에 있는 Record를 Entity간 서로 매핑해주는 인터페이스 - 1:1
 */
interface SingleRecordMapper<R : Record, E> {

    /**
     * entity로 mapping을 수행하는 함수.
     * @param record mapping을 수행할 레코드 입니다.
     * @return Entity entity로 매핑된 데이터 입니다.
     */
    fun mapToEntity(record: R): E

    /**
     * entity를 record로 mapping을 수행하는 함수
     * @param entity 특정기간 동안 수집된 엔티티 입니다.
     * @return 변환된 Record값입니다.
     */
    fun mapToRecord(entity: E): R
}