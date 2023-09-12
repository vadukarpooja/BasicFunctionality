package com.example.apicalldemo.models

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable


data class ColorsModel1(val id:Int=0,
                        var name:String= "",
                        val year:String ="",
                        val color:String ="", )

