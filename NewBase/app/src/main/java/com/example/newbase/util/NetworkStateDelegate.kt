package com.example.newbase.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

class NetworkStateDelegate(private val context: Context) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkState: MutableLiveData<Boolean> = MutableLiveData()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            networkState.postValue(true)
        }

        override fun onLost(network: Network) {
            networkState.postValue(false)
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}
