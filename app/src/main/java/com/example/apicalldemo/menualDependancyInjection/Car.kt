package com.example.apicalldemo.menualDependancyInjection


class Car {


   var engine = Engin()

    fun start(type: String) {
        engine.startEngine()

    }


}