package com.zioanacleto.speakeazy.core.analytics.di

import com.zioanacleto.speakeazy.core.analytics.network.ConnectivityManagerNetworkMonitor
import com.zioanacleto.speakeazy.core.analytics.network.NetworkMonitor
import com.zioanacleto.speakeazy.core.analytics.traces.FirebasePerformanceTracesManager
import com.zioanacleto.speakeazy.core.analytics.traces.PerformanceTracesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val analyticsModule = module {
    single<NetworkMonitor> { ConnectivityManagerNetworkMonitor(androidContext(), get()) }
    single<PerformanceTracesManager> { FirebasePerformanceTracesManager() }
}