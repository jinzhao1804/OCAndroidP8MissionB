package com.example.p8vitesse

import android.app.Application
import com.example.p8vitesse.data.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))

    }
}
