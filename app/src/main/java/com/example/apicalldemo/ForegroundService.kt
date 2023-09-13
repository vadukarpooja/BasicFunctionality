package com.example.apicalldemo

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast


class ForegroundService() : Service() {
    var countUpdate = 1
    val handler = Handler()
    val handler2 = Handler()
     var runnable: Runnable? = null
     var runnable1: Runnable? = null

    override fun onCreate() {
        super.onCreate()

        runnable =object : Runnable {
            override fun run() {
                handler.postDelayed(this, 5000)
                Log.e(javaClass.simpleName, "run: ${countUpdate++}")
                //count = countUpdate++.toString()
            }
        }
        handler.postDelayed(runnable as Runnable, 5000)
        /*SomeTaskService(this).execute() as SomeTaskService*/
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /*@SuppressLint("StaticFieldLeak")
    class SomeTaskService(
        val context: Context,
    ) : AsyncTask<String, Void, String>(){
        var count:String = ""
        override fun doInBackground(vararg strings: String): String {
            return null.toString()
        }

        override fun onPostExecute(s: String) {
            var countUpdate = 1
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    handler.postDelayed(this, 5000)
                    //count = countUpdate++.toString()
                    Log.e(javaClass.simpleName, "run: ${countUpdate++}")
                }
            }, 4000)
            val handler2 = Handler()
            handler2.postDelayed(object : Runnable {
                override fun run() {
                    handler2.postDelayed(this, 50000)
                    Toast.makeText(context,count, Toast.LENGTH_SHORT).show()
                }
            },50000)
        }

    }
*/

}

