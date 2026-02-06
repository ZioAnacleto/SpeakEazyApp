package com.zioanacleto.speakeazy.core.analytics

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}