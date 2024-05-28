package com.home.cdp2app.common.module

import com.home.cdp2app.common.memory.LocalDataStorage
import com.home.cdp2app.health.bloodpressure.mapper.BloodPressureMapper
import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import com.home.cdp2app.health.bloodpressure.repository.HealthConnectBloodPressureRepository
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import com.home.cdp2app.health.heart.repository.HealthConnectHeartRepository
import com.home.cdp2app.health.heart.repository.HeartRepository
import com.home.cdp2app.health.sleep.mapper.SleepHourMapper
import com.home.cdp2app.health.sleep.repository.HealthConnectSleepRepository
import com.home.cdp2app.health.sleep.repository.SleepRepository
import com.home.cdp2app.main.predict.api.PredictAPI
import com.home.cdp2app.main.predict.repository.PredictCacheRepository
import com.home.cdp2app.main.predict.repository.PredictRepository
import com.home.cdp2app.main.predict.repository.PreferencePredictCacheRepository
import com.home.cdp2app.main.predict.repository.RemotePredictRepository
import com.home.cdp2app.main.setting.basicinfo.repository.BasicInfoRepository
import com.home.cdp2app.main.setting.basicinfo.repository.PreferenceBasicInfoRepository
import com.home.cdp2app.main.setting.order.repository.ChartOrderRepository
import com.home.cdp2app.main.setting.order.repository.PreferenceOrderRepository
import com.home.cdp2app.tutorial.repository.PreferenceTutorialRepository
import com.home.cdp2app.tutorial.repository.TutorialRepository
import com.home.cdp2app.user.sign.api.RemoteUserAPI
import com.home.cdp2app.user.sign.repository.RemoteUserRepository
import com.home.cdp2app.user.sign.repository.UserRepository
import com.home.cdp2app.user.token.repository.AuthTokenRepository
import com.home.cdp2app.user.token.repository.PreferenceAuthTokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    /*
     *  건강정보 부분
     */

    @Provides
    @Singleton
    fun provideHeartRateRepository(healthConnectDao: HealthConnectDao, heartRateMapper: HeartRateMapper) : HeartRepository {
        return HealthConnectHeartRepository(healthConnectDao, heartRateMapper)
    }

    @Provides
    @Singleton
    fun provideSleepHourRepository(healthConnectDao: HealthConnectDao, sleepHourMapper: SleepHourMapper) : SleepRepository {
        return HealthConnectSleepRepository(sleepHourMapper, healthConnectDao)
    }

    @Provides
    @Singleton
    fun provideBloodRepository(healthConnectDao: HealthConnectDao, bloodPressureMapper: BloodPressureMapper) : BloodPressureRepository {
        return HealthConnectBloodPressureRepository(healthConnectDao, bloodPressureMapper)
    }

    //basic info
    @Provides
    @Singleton
    fun provideBasicInfoRepository(storage : LocalDataStorage) : BasicInfoRepository {
        return PreferenceBasicInfoRepository(storage)
    }

    //auth token
    @Provides
    @Singleton
    fun provideAuthTokenRepository(storage: LocalDataStorage) : AuthTokenRepository {
        return PreferenceAuthTokenRepository(storage)
    }

    //user
    @Provides
    @Singleton
    fun provideUserRepository(userAPI: RemoteUserAPI) : UserRepository {
        return RemoteUserRepository(userAPI)
    }

    //tutorial
    @Provides
    @Singleton
    fun provideTutorialRepository(storage: LocalDataStorage) : TutorialRepository {
        return PreferenceTutorialRepository(storage)
    }

    //chart order
    @Provides
    @Singleton
    fun provideChartOrderRepository(storage: LocalDataStorage) : ChartOrderRepository {
        return PreferenceOrderRepository(storage)
    }

    //predict
    @Provides
    @Singleton
    fun providePredictRepository(predictAPI: PredictAPI) : PredictRepository {
        return RemotePredictRepository(predictAPI)
    }

    @Provides
    @Singleton
    fun provideCachePredictRepository(storage: LocalDataStorage) : PredictCacheRepository {
        return PreferencePredictCacheRepository(storage)
    }

}