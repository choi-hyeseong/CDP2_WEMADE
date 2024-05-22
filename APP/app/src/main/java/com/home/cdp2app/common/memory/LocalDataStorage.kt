package com.home.cdp2app.common.memory

import com.home.cdp2app.common.memory.exception.TargetNotFoundException
import kotlin.reflect.KClass

/**
 * Local(기기)에 정보를 키-값 형식으로 저장하는 인터페이스 입니다.
 * SharedPreferences, InMemoryStorage등등이 해당 인터페이스를 구현할 수 있습니다.
 */
interface LocalDataStorage {

    /**
     * 객체를 저장하는 메소드 입니다.
     * @param key 객체를 저장할 키입니다.
     * @param value 저장될 객체입니다.
     * @return 저장의 성공여부 입니다.
     */
    suspend fun saveObject(key : String, value : Any) : Boolean

    /**
     * 저장된 객체를 불러오는 메소드 입니다.
     * @param key 불러올 객체의 키값입니다.
     * @param targetClass 객체의 클래스 타입입니다.
     * @throws TargetNotFoundException 해당 key에 저장된 값이 없을경우 발생하는 Exception 입니다.
     * @throws IllegalArgumentException 해당 key에 저장된 값은 있으나, targetClass로 deserialize 할 수 없는경우 발생합니다.
     * @return targetClass로 역직렬화 된 객체 T를 반환합니다.
     */
    suspend fun <T : Any> loadObject(key : String, targetClass : KClass<T>) : T

    /**
     * 해당 key에 저장된 값을 제거합니다.
     * @param key 값이 저장된 key 입니다.
     * @return 삭제가 이루어졌는지 결과값입니다.
     */
    suspend fun delete(key : String) : Boolean
    /**
     * @param key Int값을 저장할 키값입니다.
     * @param value 저장될 Int값입니다.
     * @return 저장의 성공 여부입니다.
     */
    suspend fun putInt(key : String, value : Int) : Boolean

    /**
     * @param key Int값을 불러올 키 값입니다.
     * @param defaultValue 해당 키값이 없을경우 불러올 default 값입니다.
     * @return 불러온 Int값 입니다. 만약 키에 저장된 Int값이 없을경우 defaultValue를 반환합니다.
     */
    suspend fun getInt(key : String, defaultValue : Int) : Int

    /**
     * @param key Double값을 저장할 키값입니다.
     * @param value 저장될 Double 값입니다.
     * @return 저장의 성공 여부입니다.
     */
    suspend fun putDouble(key : String, value : Double) : Boolean

    /**
     * @param key Double값을 불러올 키 값입니다.
     * @param defaultValue 해당 키값이 없을경우 불러올 default 값입니다.
     * @return 불러온 Double값 입니다. 만약 키에 저장된 Double값이 없을경우 defaultValue를 반환합니다.
     */
    suspend fun getDouble(key : String, defaultValue : Double) : Double

    /**
     * @param key Boolean값을 불러올 키 값입니다.
     * @param defaultValue 해당 키값이 없을경우 불러올 default 값입니다.
     * @return 불러온 Boolean값 입니다. 만약 키에 저장된 Boolean값이 없을경우 defaultValue를 반환합니다.
     */
    suspend fun getBoolean(key : String, defaultValue: Boolean) : Boolean


    /**
     * @param key Boolean값을 저장할 키값입니다.
     * @param value 저장될 Boolean 값입니다.
     * @return 저장의 성공 여부입니다.
     */
    suspend fun putBoolean(key : String, value : Boolean) : Boolean


}