package com.example.apicalldemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import java.security.acl.Owner


class ForegroundService() : Service() {
    lateinit var mSmsReceiver: Receiver
    private val binder: IBinder = MyBinder()
    val handler = Handler()
    val isKill:Boolean = false
    private var runnable: Runnable? = null
    var count= 1
    lateinit var application: MainActivity
    val liveData = MutableLiveData<Int?>()

    override fun onCreate() {
        if (SharedPrefs.getValue(this@ForegroundService,"count",0) == 0){
            task()
        }
        else{
            val result: Int = SharedPrefs.getValue(this@ForegroundService,"count",0) as Int
            task(result)
        }
        Log.e(javaClass.simpleName, "onCreate: count"+ SharedPrefs.getValue(this@ForegroundService,"count",0) )
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(javaClass.simpleName, "onStartCommand: ")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.e(javaClass.simpleName, "onDestroy: ")
        super.onDestroy()
    }


    override fun onBind(intent: Intent): IBinder {
        Log.e(javaClass.simpleName, "onBind: ")
        return binder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e(javaClass.simpleName, "onTaskRemoved count: "+ SharedPrefs.getValue(this@ForegroundService,"count",0) )
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(javaClass.simpleName, "onUnbind: ")
        return super.onUnbind(intent)
    }

    // create an inner Binder class
    class MyBinder : Binder() {
        fun getService(): ForegroundService {
            return ForegroundService()
        }
    }

     private fun task(countUpdate: Int? = null){
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
                SharedPrefs.setValue(this@ForegroundService,"count",result)
                SharedPrefs.setValue(this@ForegroundService,"result",result.toString())
                Log.e(javaClass.simpleName, "run: ${result}")
            }
        }, 4000)
    }
   /* @SuppressLint("StaticFieldLeak")
    class SomeTaskService(
        val context: Context,
    ) : AsyncTask<Int, Void, Int>() {
        var count = 1
        override fun doInBackground(vararg params: Int?): Int? {
            return params[0]
        }

        override fun onPostExecute(result: Int) {
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    handler.postDelayed(this, 5000)
                    val countUpdate = count++.toString()
                    SharedPrefs.setValue(context,"count",countUpdate)
                    Log.e(javaClass.simpleName, "run: $countUpdate")
                }
            }, 4000)

        }

    }*/

}
