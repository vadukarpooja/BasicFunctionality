package com.example.apicalldemo

import android.app.Application
import android.content.Context
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication :Application(){

    override fun onCreate() {
        super.onCreate()

    }

}
