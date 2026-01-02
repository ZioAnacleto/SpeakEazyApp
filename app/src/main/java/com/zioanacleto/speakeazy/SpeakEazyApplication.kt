package com.zioanacleto.speakeazy

import android.app.Application
import android.content.Context
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.di.dataMapperModule
import com.zioanacleto.speakeazy.di.dataSourceModule
import com.zioanacleto.speakeazy.di.databaseModule
import com.zioanacleto.speakeazy.di.repositoryModule
import com.zioanacleto.speakeazy.di.singletonModule
import com.zioanacleto.speakeazy.di.viewModelModule
import com.zioanacleto.speakeazy.domain.logger.LocalLogger
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