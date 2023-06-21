package com.example.apicalldemo.api

import com.example.apicalldemo.ResponseClass
import retrofit2.Response


interface ApiHelper {
    suspend fun getColorList():Response<ResponseClass>
}