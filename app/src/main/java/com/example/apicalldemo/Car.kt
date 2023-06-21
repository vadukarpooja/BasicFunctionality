package com.example.apicalldemo

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class Car {


   var engine = Engin()

    fun start(type: String) {
        engine.startEngine()

    }


}