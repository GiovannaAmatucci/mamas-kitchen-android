package com.giovanna.amatucci.foodbook.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.giovanna.amatucci.foodbook.R
import java.io.IOException

class NoConnectivityException : IOException(R.string.error_no_internet.toString())

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET
    ) && capabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_VALIDATED
    ) && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}