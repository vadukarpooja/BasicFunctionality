package com.example.apicalldemo.menualDependancyInjection


class Car2(private  var engin: Engin)  {
    fun start(type:String){
       engin.startEngine2()

    }
}