package com.example.apicalldemo.api

import com.example.apicalldemo.ResponseClass
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("unknown")
    suspend fun getColorsList(): Response<ResponseClass>

}