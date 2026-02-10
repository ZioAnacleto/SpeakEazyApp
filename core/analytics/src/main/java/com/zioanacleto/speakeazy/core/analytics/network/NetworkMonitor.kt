package com.zioanacleto.speakeazy.core.analytics.network

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}