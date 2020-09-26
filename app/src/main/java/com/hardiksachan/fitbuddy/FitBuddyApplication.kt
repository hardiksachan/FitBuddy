package com.hardiksachan.fitbuddy

import android.app.Application
import timber.log.Timber

class FitBuddyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}