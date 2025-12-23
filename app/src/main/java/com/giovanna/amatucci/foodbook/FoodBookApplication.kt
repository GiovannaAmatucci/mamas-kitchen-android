package com.giovanna.amatucci.foodbook

import android.app.Application
import com.giovanna.amatucci.foodbook.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class FoodBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupTimber()
    }
    private fun setupTimber() {
        if (BuildConfig.DEBUG_MODE) {
            Timber.plant(Timber.DebugTree())
        }
    }
    private fun setupKoin() {
        startKoin {
            if (BuildConfig.DEBUG_MODE) androidLogger(Level.DEBUG) else androidLogger(Level.NONE)
            androidContext(this@FoodBookApplication)
            modules(appModules)
        }
    }
}