package com.example.apicalldemo.api

import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ColorsModel1
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("unknown")
    suspend fun getColorsList(): Response<ResponseClass>

    @GET("unknown")
    fun getMovieList():Call<ResponseClass>

}