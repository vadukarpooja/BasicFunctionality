package com.example.apicalldemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


open class Receiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent?.getStringExtra("count")
        Log.e(javaClass.simpleName, "onReceive: $data")
    }
}