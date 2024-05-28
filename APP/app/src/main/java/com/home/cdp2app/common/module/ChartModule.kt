package com.home.cdp2app.common.module

import com.home.cdp2app.main.dashboard.chart.parser.ChartParser
import com.home.cdp2app.main.dashboard.chart.parser.mapper.BloodPressureDiastolicChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.BloodPressureSystolicChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.ChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.HeartRateChartMapper
import com.home.cdp2app.main.dashboard.chart.parser.mapper.SleepHourChartMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//chart 매핑 관련 모듈
@Module
@InstallIn(SingletonComponent::class)
class ChartModule {

    @Provides
    @Singleton
    fun provideHeartChartMapper() : HeartRateChartMapper {
        return HeartRateChartMapper()
    }

    @Provides
    @Singleton
    fun provideSleepHourChartMapper() : SleepHourChartMapper {
        return SleepHourChartMapper()
    }

    @Provides
    @Singleton
    fun provideSystolicChartMapper() : BloodPressureSystolicChartMapper {
        return BloodPressureSystolicChartMapper()
    }

    @Provides
    @Singleton
    fun provideDiastolicChartMapper() : BloodPressureDiastolicChartMapper {
        return BloodPressureDiastolicChartMapper()
    }

    @Provides
    @Singleton
    fun provideChartParser(heart : HeartRateChartMapper, sleep : SleepHourChartMapper, diastolic : BloodPressureDiastolicChartMapper, systolic : BloodPressureSystolicChartMapper) : ChartParser {
        // wildcard generic으로 한방에 받는 방법이 있을거 같은데..
        return ChartParser(listOf(heart, sleep, diastolic, systolic))
    }
}