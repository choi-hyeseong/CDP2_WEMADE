package com.home.cdp2app.view.chart.parser

import com.home.cdp2app.health.order.type.HealthCategory
import com.home.cdp2app.view.chart.Chart
import com.home.cdp2app.view.chart.parser.mapper.ChartMapper

/**
 * List<T> 의 형태로 된 객체를 Chart로 변환해주는 Parser
 * 등록된 ChartMapper가 지원할경우 수행됩니다.
 * @property mappers 파서에서 사용될 mapper들의 리스트입니다. isSupports에서 true를 반환한 mapper를 사용합니다.
 */
class ChartParser(private val mappers: List<ChartMapper<*>>) {

    /**
     * 파싱을 수행하는 함수입니다.
     * @param data List로 구성된 어떤 객체입니다. 아무 객체의 리스트를 받습니다.
     * @param requestEnum data가 어떤 카테고리로 변환될지 지정하는 파라미터 입니다. 같은 엔티티라도 다른 차트로 변할 수 있기 때문에 변환할 카테고리를 지정해주어야 합니다.
     * @return 파싱된 Chart를 반환합니다.
     * @throws IllegalArgumentException param의 data가 비어있을경우 발생합니다.
     * @throws IllegalStateException param의 data에 맞는 mapper가 등록되지 않았을경우 발생합니다. (property의 mappers에 등록)
     */
    fun parse(data: List<*>, requestEnum : HealthCategory): Chart {
        // List가 비어있는경우 타입 확인 어려움 + 변환하는 의미 없음
        if (data.isEmpty()) throw IllegalArgumentException("data는 비어있어선 안됩니다.")
        //리스트가 비어있지 않으므로, 첫번째 object를 꺼낸뒤, 클래스 타입 비교 수행
        val matchedMapper = mappers.find { it.isSupports(data.first()!!::class) && it.isConvertTo() == requestEnum } ?: throw IllegalStateException("data를 지원하는 Mapper가 등록되지 않았습니다.")
        //matchedMapper가 data를 지원하므로 convert 호출가능하게 캐스팅 후 실행
        //하지만 wildcard로 선언되어 있기 때문에 타입을 알 수 없음. 따라서 Nothing으로 받기 때문에 캐스팅 진행.
        return matchedMapper.convertToChart(data as List<Nothing>)
    }
}
