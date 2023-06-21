package com.example.apicalldemo

import android.util.Log
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class Engin {

    fun startEngine(){
        Log.e(javaClass.simpleName, "startEngine", )
    }
    fun startEngine2(){
        Log.e(javaClass.simpleName, "startEngine2", )
    }
}