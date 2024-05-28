package com.home.cdp2app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CDP2Application : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}