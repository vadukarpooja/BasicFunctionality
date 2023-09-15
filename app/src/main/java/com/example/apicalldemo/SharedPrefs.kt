package com.example.apicalldemo

import android.annotation.SuppressLint
import android.content.Context

class SharedPrefs {
    companion object {
        @SuppressLint("SuspiciousIndentation")
        fun setValue(context: Context, key:String, value:Any?) {
            val sharedPref=context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            when(value) {
                is Int -> sharedPref.edit().putInt(key, value).apply()
                is String -> sharedPref.edit().putString(key, value).apply()
            }
        }
        fun getValue(context: Context, key:String, defaultVal:Any):Any {
            val sharedPref=context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            when(defaultVal) {
                is Int -> return sharedPref.getInt(key, defaultVal)
                is String -> return sharedPref.getString(key, defaultVal)?:""
                else-> return 0
            }
        }

        fun remove(context: Context, key: String){
            val sharedPref=context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            sharedPref.edit().remove(key).apply()
        }

        fun clear(context: Context){
            val sharedPref=context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()
        }
    }
}