package com.example.newdownloader26.core.managers

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetManager(private val connectivityManager: ConnectivityManager) {

    val isInternetConnected: Boolean
        get() {
            return try {
                val network = connectivityManager.activeNetwork ?: return false
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network) ?: return false

                val hasInternetTransport =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

                val hasVpn = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

                hasInternetTransport || (hasVpn && connectivityManager.allNetworks.any {
                    val caps = connectivityManager.getNetworkCapabilities(it)
                    caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                        caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                        caps?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true
                })
            } catch (_: Exception) {
                false
            }
        }
}

