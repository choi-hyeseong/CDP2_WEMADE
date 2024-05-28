package com.home.cdp2app.main.dashboard.chart.parser.mapper

import com.home.cdp2app.main.dashboard.chart.Chart
import com.home.cdp2app.main.setting.order.type.HealthCategory
import kotlin.reflect.KClass

/**
 * Entity를 Chart로 매핑시키는 클래스
 */
abstract class ChartMapper<T> {

    /**
     * 1:1로 매핑된 엔티티인 T를 chart로 변환시키는 함수
     */
    abstract fun convertToChart(entities: List<T>): Chart

    /**
     * 어떤 클래스의 차트 매핑을 지원할지 확인하는 함수. ChartParser.kt에서 사용됨
     * @param targetClass 해당 클래스의 차트 변환을 지원할지 확인하는 파라미터 입니다. wildcard로 지정되어 모든 오브젝트 (클래스)를 지원합니다
     * @return targetClass를 지원할경우 true, 아닐경우 false를 반환합니다.
     */
    abstract fun isSupports(targetClass : KClass<*>) : Boolean

    /**
     * 위 isSupports의 중복 문제를 해결하기 위한 메소드 입니다. 만약 isSupports가 같은 값은 반환하는 다른 클래스 2개 (BloodPressureDiastolic, Systolic..)이 있을경우 구분할 수 없습니다.
     * 따라서, isConvertTo를 이용해서 어떤 카테고리로 매핑되는지 지정합니다.
     * @see ChartParser
     * @return 변환되는 차트의 카테고리를 반환합니다.
     */
    abstract fun isConvertTo() : HealthCategory
}