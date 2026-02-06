package com.zioanacleto.speakeazy

import android.app.Application
import android.content.Context
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.analytics.di.analyticsModule
import com.zioanacleto.speakeazy.core.data.di.dataMapperModule
import com.zioanacleto.speakeazy.core.data.di.dataSourceModule
import com.zioanacleto.speakeazy.core.data.di.repositoryModule
import com.zioanacleto.speakeazy.core.database.di.databaseModule
import com.zioanacleto.speakeazy.di.singletonModule
import com.zioanacleto.speakeazy.di.viewModelModule
import com.zioanacleto.speakeazy.logger.LocalLogger
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module

class SpeakEazyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Koin section
        startKoin {
            androidLogger()
            modules(
                listOf(
                    singletonModule,
                    databaseModule,
                    viewModelModule,
                    dataMapperModule,
                    dataSourceModule,
                    repositoryModule,
                    analyticsModule,
                    module {
                        single { this@SpeakEazyApplication } binds arrayOf(
                            Context::class,
                            Application::class
                        )
                    }
                )
            )
        }

        // Logger section
        AnacletoLogger.registerAnacletoLogger(LocalLogger())
    }
}