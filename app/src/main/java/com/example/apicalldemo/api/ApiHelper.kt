package com.example.apicalldemo.api

import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import retrofit2.Response


interface ApiHelper {
    suspend fun getColorList():Response<ResponseClass>

    suspend fun getMoviesList():Response<ArrayList<ColorsModel>>
}