package com.example.apicalldemo

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager

interface CountInterFace {
    fun onUpdateCount(count:Int, localBroadcastManager: LocalBroadcastManager, context: Context){

    }
}