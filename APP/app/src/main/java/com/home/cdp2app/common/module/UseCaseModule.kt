package com.home.cdp2app.common.module

import com.home.cdp2app.health.bloodpressure.repository.BloodPressureRepository
import com.home.cdp2app.health.bloodpressure.usecase.LoadBloodPressure
import com.home.cdp2app.health.bloodpressure.usecase.SaveBloodPressure
import com.home.cdp2app.health.heart.repository.HeartRepository
import com.home.cdp2app.health.heart.usecase.LoadHeartRate
import com.home.cdp2app.health.heart.usecase.SaveHeartRate
import com.home.cdp2app.health.sleep.repository.SleepRepository
import com.home.cdp2app.health.sleep.usecase.LoadSleepHour
import com.home.cdp2app.health.sleep.usecase.SaveSleepHour
import com.home.cdp2app.main.predict.repository.PredictCacheRepository
import com.home.cdp2app.main.predict.repository.PredictRepository
import com.home.cdp2app.main.predict.usecase.GetCachePredictResult
import com.home.cdp2app.main.predict.usecase.PredictUseCase
import com.home.cdp2app.main.predict.usecase.SaveCachePredictResult
import com.home.cdp2app.main.setting.basicinfo.repository.BasicInfoRepository
import com.home.cdp2app.main.setting.basicinfo.usecase.HasBasicInfo
import com.home.cdp2app.main.setting.basicinfo.usecase.LoadBasicInfo
import com.home.cdp2app.main.setting.basicinfo.usecase.SaveBasicInfo
import com.home.cdp2app.main.setting.order.repository.ChartOrderRepository
import com.home.cdp2app.main.setting.order.usecase.LoadChartOrder
import com.home.cdp2app.main.setting.order.usecase.SaveChartOrder
import com.home.cdp2app.tutorial.repository.TutorialRepository
import com.home.cdp2app.tutorial.usecase.CheckTutorialCompleted
import com.home.cdp2app.tutorial.usecase.SaveTutorialCompleted
import com.home.cdp2app.user.sign.repository.UserRepository
import com.home.cdp2app.user.sign.usecase.LoginUseCase
import com.home.cdp2app.user.sign.usecase.RegisterUseCase
import com.home.cdp2app.user.token.repository.AuthTokenRepository
import com.home.cdp2app.user.token.usecase.DeleteAuthToken
import com.home.cdp2app.user.token.usecase.GetAuthToken
import com.home.cdp2app.user.token.usecase.HasAuthToken
import com.home.cdp2app.user.token.usecase.SaveAuthToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideLoadBloodPressure(repository: BloodPressureRepository) : LoadBloodPressure {
        return LoadBloodPressure(repository)
    }

    @Provides
    @Singleton
    fun provideSaveBloodPressure(repository: BloodPressureRepository) : SaveBloodPressure {
        return SaveBloodPressure(repository)
    }

    @Provides
    @Singleton
    fun provideLoadHeartRate(repository: HeartRepository) : LoadHeartRate {
        return LoadHeartRate(repository)
    }

    @Provides
    @Singleton
    fun provideSaveHeartRate(repository: HeartRepository) : SaveHeartRate {
        return SaveHeartRate(repository)
    }

    @Provides
    @Singleton
    fun provideLoadSleepHour(repository : SleepRepository) : LoadSleepHour {
        return LoadSleepHour(repository)
    }

    @Provides
    @Singleton
    fun provideSaveSleepHour(repository: SleepRepository) : SaveSleepHour {
        return SaveSleepHour(repository)
    }

    @Provides
    @Singleton
    fun provideHasBasicInfo(repository : BasicInfoRepository) : HasBasicInfo {
        return HasBasicInfo(repository)
    }

    @Provides
    @Singleton
    fun provideSaveBasicInfo(repository: BasicInfoRepository) : SaveBasicInfo {
        return SaveBasicInfo(repository)
    }

    @Provides
    @Singleton
    fun provideLoadBasicInfo(repository: BasicInfoRepository) : LoadBasicInfo {
        return LoadBasicInfo(repository)
    }

    @Provides
    @Singleton
    fun provideLoadChartOrder(repository : ChartOrderRepository) : LoadChartOrder {
        return LoadChartOrder(repository)
    }

    @Provides
    @Singleton
    fun provideSaveChartOrder(repository: ChartOrderRepository) : SaveChartOrder {
        return SaveChartOrder(repository)
    }

    @Provides
    @Singleton
    fun provideTutorialCheck(repository : TutorialRepository) : CheckTutorialCompleted {
        return CheckTutorialCompleted(repository)
    }

    @Provides
    @Singleton
    fun provideSaveTutorial(repository: TutorialRepository) : SaveTutorialCompleted {
        return SaveTutorialCompleted(repository)
    }

    @Provides
    @Singleton
    fun provideLogin(repository: UserRepository) : LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegister(repository: UserRepository) : RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteToken(repository : AuthTokenRepository) : DeleteAuthToken {
        return DeleteAuthToken(repository)
    }

    @Provides
    @Singleton
    fun provideGetToken(repository: AuthTokenRepository) : GetAuthToken {
        return GetAuthToken(repository)
    }

    @Provides
    @Singleton
    fun provideHasAuthToken(repository : AuthTokenRepository) : HasAuthToken {
        return HasAuthToken(repository)
    }

    @Provides
    @Singleton
    fun provideSaveAuthToken(repository: AuthTokenRepository) : SaveAuthToken {
        return SaveAuthToken(repository)
    }

    @Provides
    @Singleton
    fun provideGetCachePredict(repository : PredictCacheRepository) : GetCachePredictResult {
        return GetCachePredictResult(repository)
    }

    @Provides
    @Singleton
    fun provideSaveCachePredict(repository: PredictCacheRepository) : SaveCachePredictResult {
        return SaveCachePredictResult(repository)
    }

    @Provides
    @Singleton
    fun providePredict(getAuthToken: GetAuthToken, loadBasicInfo: LoadBasicInfo, loadHeartRate: LoadHeartRate, loadSleepHour: LoadSleepHour, loadBloodPressure: LoadBloodPressure, repository: PredictRepository) : PredictUseCase {
        return PredictUseCase(getAuthToken, loadBasicInfo, loadSleepHour, loadHeartRate, loadBloodPressure, repository)
    }
}