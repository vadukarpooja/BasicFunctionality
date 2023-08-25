package com.example.apicalldemo

import android.app.Application
import android.content.Context
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


@HiltAndroidApp
class BaseApplication :Application(){
    var socket: Socket? = null

    init {
        try {
            socket = IO.socket(CHAT_SERVER_URL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }
    override fun onCreate() {
        super.onCreate()

    }

}
