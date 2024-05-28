package com.home.cdp2app.common.module

import com.home.cdp2app.health.bloodpressure.valid.BloodPressureValidator
import com.home.cdp2app.health.heart.valid.HeartRateValidator
import com.home.cdp2app.health.sleep.valid.SleepHourValidator
import com.home.cdp2app.main.dashboard.view.dialog.blood.validator.BloodPressureViewValidator
import com.home.cdp2app.main.dashboard.view.dialog.heart.validator.HeartRateViewValidator
import com.home.cdp2app.main.dashboard.view.dialog.sleep.validator.SleepHourViewValidator
import com.home.cdp2app.user.sign.validator.LoginValidator
import com.home.cdp2app.user.sign.validator.RegisterValidator
import com.home.cdp2app.user.sign.view.login.validator.LoginViewValidator
import com.home.cdp2app.user.sign.view.register.validator.RegisterViewValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ValidatorModule {

    @Provides
    @Singleton
    fun provideHeartValidator() : HeartRateValidator {
        return HeartRateValidator()
    }

    @Provides
    @Singleton
    fun provideHeartViewValidator(validator : HeartRateValidator) : HeartRateViewValidator {
        return HeartRateViewValidator(validator)
    }

    @Provides
    @Singleton
    fun provideSleepHourValidator() : SleepHourValidator {
        return SleepHourValidator()
    }

    @Provides
    @Singleton
    fun provideSleepViewValidator(validator: SleepHourValidator) : SleepHourViewValidator {
        return SleepHourViewValidator(validator)
    }

    @Provides
    @Singleton
    fun provideBloodValidator() : BloodPressureValidator {
        return BloodPressureValidator()
    }

    @Provides
    @Singleton
    fun provideBloodViewValidator(validator: BloodPressureValidator) : BloodPressureViewValidator {
        return BloodPressureViewValidator(validator)
    }

    @Provides
    @Singleton
    fun provideLoginValidator() : LoginValidator {
        return LoginValidator()
    }

    @Provides
    @Singleton
    fun provideLoginViewValidator(validator: LoginValidator) : LoginViewValidator {
        return LoginViewValidator(validator)
    }

    @Provides
    @Singleton
    fun provideRegisterValidator() : RegisterValidator {
        return RegisterValidator()
    }

    @Provides
    @Singleton
    fun provideRegisterViewValidator(validator: RegisterValidator) : RegisterViewValidator {
        return RegisterViewValidator(validator)
    }

}