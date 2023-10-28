package com.example.newbase.util

interface NetworkStateObserver {
    fun onNetworkAvailable()
    fun onNetworkUnavailable()
}
