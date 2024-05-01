package com.home.cdp2app.util.json

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.reflect.KClass

/**
 * Json 객체 읽고 쓰기용 유틸리티 클래스
 */
class JsonMapperUtil {
    companion object {
        //kotlin 전용 mapper
        private val mapper = jacksonObjectMapper().apply {
            registerModule(JavaTimeModule()) //Instant등의 시간 매핑 지원
        }

        /**
         * String json를 이용해 Object로 역직렬화 하는 메소드입니다.
         * @param value String으로 Serialize되어 있는 object 입니다.
         * @param target 역직렬화될 클래스 입니다.
         * @return 역직렬화 된 결과값 입니다.
         *  @throws JsonProcessingException json의 구조가 올바르지 않을때 발생합니다.
         * @throws JsonMappingException json이 해당 클래스와 맞지 않는경우 발생합니다.
         */
        fun <T : Any> readObject(value : String, target : KClass<T>) : T {
            return mapper.readValue(value, target.java)
        }

        /**
         * Object를 String으로 직렬화 하는 메소드 입니다.
         * @param value String으로 직렬화 할 Object입니다.
         * @return 직렬화 된 String 입니다.
         * @throws JsonProcessingException 직렬화 과정에서 발생할 수 있는 IOException등등이 포함된 예외입니다.
         */
        fun writeToString(value : Any) : String {
            return mapper.writeValueAsString(value)
        }
    }

}