package com.example.newbase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.newbase.data.dataSource.local.DatabaseBuilder
import com.example.newbase.util.NetworkStateDelegate
import com.example.newbase.util.NetworkStateObserver
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

@HiltAndroidApp
class MyApp: Application() {

    @Inject
    lateinit var networkStateDelegate: NetworkStateDelegate

    override fun onCreate() {
        super.onCreate()
        DatabaseBuilder.initDB(applicationContext)
    }

}
