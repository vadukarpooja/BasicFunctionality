package com.example.apicalldemo

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class ForegroundService() : Service() {
    var count= 1
    var countInterFace: CountInterFace? =null
    private var mActivityMessenger: Messenger? = null
    val LOCATION_MESSAGE = 100
    lateinit var listFragment: ListFragment
    lateinit var localBroadcastManager:LocalBroadcastManager


    override fun onCreate() {
        listFragment = ListFragment()
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        if (SharedPrefs.getValue(this@ForegroundService,"count",0) == 0){
            task(context = this@ForegroundService)
        }
        else{
            val result: Int = SharedPrefs.getValue(this@ForegroundService,"count",0) as Int
            task(result, context = this@ForegroundService)
        }
        Log.e(javaClass.simpleName, "onCreate: count"+ SharedPrefs.getValue(this@ForegroundService,"count",0) )
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(javaClass.simpleName, "onStartCommand: ")
        mActivityMessenger =
            intent.getParcelableExtra(ListFragment.MESSENGER_INTENT_KEY)
        return START_STICKY
    }

    override fun onDestroy() {
        Log.e(javaClass.simpleName, "onDestroy: ")
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder? {
        Log.e(javaClass.simpleName, "onBind: ")
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e(javaClass.simpleName, "onTaskRemoved count: "+ SharedPrefs.getValue(this@ForegroundService,"count",0) )
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(javaClass.simpleName, "onUnbind: ")
        return super.onUnbind(intent)
    }
    private fun task(countUpdate: Int? = null,context: Context){
         var result: Int? = countUpdate
         val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 5000)
                if (countUpdate == null){
                    result = count++
                } else{
                    result = result!! + 1
                }
                listFragment.onUpdateCount(result!!,localBroadcastManager,context)
               // countInterFace?.onUpdateCount(result!!)
                SharedPrefs.setValue(this@ForegroundService,"count",result)
                Log.e(javaClass.simpleName, "run: ${result}")
            }
        }, 4000)
    }
    private fun sendMessage(messageID: Int, count: Int) {
        /**  If this service is launched by the JobScheduler, there's no callback Messenger. It
        only exists when the MainActivity calls startService() with the callback in the Intent.*/
        if (mActivityMessenger == null) {

            return
        }
        val m = Message.obtain()
        m.what = messageID
        m.obj = count
        try {
            mActivityMessenger!!.send(m)
        } catch (e: RemoteException) {
            Log.e(javaClass.simpleName, "Error passing service object back to activity.")
        }
    }
}
