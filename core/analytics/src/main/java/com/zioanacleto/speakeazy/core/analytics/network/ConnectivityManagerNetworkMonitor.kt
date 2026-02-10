package com.zioanacleto.speakeazy.core.analytics.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import androidx.core.content.getSystemService
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

/**
 *  This [NetworkMonitor] implementation keeps checking for all available networks, and if there are none
 *  emits a false [Flow]<[Boolean]>
 */
class ConnectivityManagerNetworkMonitor(
    private val context: Context,
    dispatcherProvider: DispatcherProvider
) : NetworkMonitor {
    override val isOnline: Flow<Boolean> = callbackFlow {
        // Get the ConnectivityManager from the Context
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        // We create a network callback to listen for all possible networks changes
        val callback = object : ConnectivityManager.NetworkCallback() {
            private val networks = mutableSetOf<Network>()
            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                channel.trySend(networks.isNotEmpty())
            }
        }

        // we need to register the callback to the connectivityManager
        val request = Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)

        channel.trySend(connectivityManager.isCurrentlyConnected())

        // We unregister the callback when the flow is closed
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        .flowOn(dispatcherProvider.io())
        .conflate()

    // If network has internet capability, returns true
    private fun ConnectivityManager.isCurrentlyConnected(): Boolean {
        val networkCapabilities = getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}