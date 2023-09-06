package com.example.apicalldemo.api

import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService):ApiHelper {
    override suspend fun getColorList(): Response<ResponseClass> = apiService.getColorsList()
    override suspend fun getMoviesList(): Response<ArrayList<ColorsModel>> = apiService.getMoviesList()

}