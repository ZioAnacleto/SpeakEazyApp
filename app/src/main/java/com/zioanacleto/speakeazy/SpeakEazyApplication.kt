package com.zioanacleto.speakeazy

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.analytics.di.analyticsModule
import com.zioanacleto.speakeazy.core.data.di.dataMapperModule
import com.zioanacleto.speakeazy.core.data.di.dataSourceModule
import com.zioanacleto.speakeazy.core.data.di.repositoryModule
import com.zioanacleto.speakeazy.core.data.search.SearchQueriesCleanUpWorker
import com.zioanacleto.speakeazy.core.database.di.databaseModule
import com.zioanacleto.speakeazy.di.singletonModule
import com.zioanacleto.speakeazy.di.viewModelModule
import com.zioanacleto.speakeazy.logger.LocalLogger
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

class SpeakEazyApplication : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(getKoin().get())
            .build()

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
            workManagerFactory()
        }

        // Logger section
        AnacletoLogger.registerAnacletoLogger(LocalLogger())

        // Workers section
        scheduleSearchCleanup(this)
    }

    private fun scheduleSearchCleanup(context: Context) {
        val workRequest =
            PeriodicWorkRequestBuilder<SearchQueriesCleanUpWorker>(
                1,
                TimeUnit.DAYS
            ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "search_queries_cleanup_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}