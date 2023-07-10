package com.example.apicalldemo

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class Car2(private  var engin: Engin)  {
    fun start(type:String){
       engin.startEngine2()

    }
}