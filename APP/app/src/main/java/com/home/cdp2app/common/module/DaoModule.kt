package com.home.cdp2app.common.module

import android.content.Context
import com.home.cdp2app.common.memory.LocalDataStorage
import com.home.cdp2app.common.memory.SharedPreferencesStorage
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext context : Context) : LocalDataStorage {
        //preference 사용하는 stroage 반환, context는 application context 사용
        return SharedPreferencesStorage(context)
    }

    @Provides
    @Singleton
    fun provideHealthConnectDao(@ApplicationContext context: Context) : HealthConnectDao {
        // Healthconnect 접근하는 dao
        return HealthConnectDao(context)
    }

    @Provides
    @Singleton
    fun provideHeartMapper() : HeartRateMapper {
        // health connect dao -> entity 매퍼
        return HeartRateMapper()
    }

    @Provides
    @Singleton
    fun provideSleepHourMapper() : SleepHourMapper {
        // health connect dao -> entity
        return SleepHourMapper()
    }

    @Provides
    @Singleton
    fun provideBloodMapper() : BloodPressureMapper {
        // health connect dao -> entity
        return BloodPressureMapper()
    }
}