package com.gps_navigator.di

import android.content.Context
import com.gps_navigator.di.koin_modules.AppModule
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ConnectKoinModules {
    val appModule = module {
        scope(named<Context>()) {
            scoped { AppModule().applicationContext(context = androidApplication()) }
        }
    }
}