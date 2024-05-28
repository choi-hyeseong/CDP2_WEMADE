package com.home.cdp2app.common.memory

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.home.cdp2app.common.memory.exception.TargetNotFoundException
import com.home.cdp2app.common.util.json.JsonMapperUtil
import kotlin.reflect.KClass

const val PREFERENCE_KEY = "HEALTH_APP_PREFERENCE" //preference 파일 키값. 고정

/**
 * SharedPreferences를 사용하는 LocalDataStroage 클래스
 * @constructor context SharedPreference를 불러오기 위한 Application Context 입니다.
 * @property sharedPreferences 실제 사용될 SharedPreference 입니다.
 */
class SharedPreferencesStorage(context : Context) : LocalDataStorage {

    private val sharedPreferences : SharedPreferences by lazy {
        //private 모드로 PREFERENCE_KEY를 가진 preference 접근
        context.getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE)
    }

    override suspend fun saveObject(key: String, value: Any): Boolean {
        return sharedPreferences.edit().putString(key, JsonMapperUtil.writeToString(value)).commit()
    }

    override suspend fun <T : Any> loadObject(key: String, targetClass: KClass<T>): T {
        //key값이 저장되어 있지 않거나, String형식이 아닌경우 Exception
        if (!sharedPreferences.contains(key) || sharedPreferences.getString(key, null) == null)
            throw TargetNotFoundException("해당 key값에 저장된 값이 없습니다.")
        try {
            //getString이 notNull임은 위 if문에서 보증
            return JsonMapperUtil.readObject(sharedPreferences.getString(key, null)!!, targetClass)
        }
        catch (e : Exception) {
            throw IllegalArgumentException("해당 객체로 역직렬화 할 수 없습니다. ${targetClass.simpleName} | ${e.message}")
        }
    }

    override suspend fun delete(key: String) : Boolean {
       return sharedPreferences.edit().remove(key).commit()
    }

    override suspend fun putInt(key: String, value: Int): Boolean {
        return sharedPreferences.edit().putInt(key, value).commit()
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override suspend fun putDouble(key: String, value: Double): Boolean {
        return sharedPreferences.edit().putFloat(key, value.toFloat()).commit()
    }

    override suspend fun getDouble(key: String, defaultValue: Double) : Double {
        return sharedPreferences.getFloat(key, defaultValue.toFloat()).toDouble()
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override suspend fun putBoolean(key: String, value: Boolean): Boolean {
        return sharedPreferences.edit().putBoolean(key, value).commit()
    }

}