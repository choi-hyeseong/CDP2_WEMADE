package com.home.cdp2app.common.module

import com.home.cdp2app.main.dashboard.view.dialog.blood.validator.BloodPressureViewValidator
import com.home.cdp2app.main.dashboard.view.dialog.factory.DialogFactory
import com.home.cdp2app.main.dashboard.view.dialog.heart.validator.HeartRateViewValidator
import com.home.cdp2app.main.dashboard.view.dialog.sleep.validator.SleepHourViewValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Provides
    @Singleton
    fun provideDialogFactory(heartRateViewValidator: HeartRateViewValidator, sleepHourViewValidator: SleepHourViewValidator, bloodPressureViewValidator: BloodPressureViewValidator) : DialogFactory {
        return DialogFactory(heartRateViewValidator, bloodPressureViewValidator, sleepHourViewValidator)
    }
}