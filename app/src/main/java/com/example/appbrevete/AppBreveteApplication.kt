package com.example.appbrevete

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppBreveteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
