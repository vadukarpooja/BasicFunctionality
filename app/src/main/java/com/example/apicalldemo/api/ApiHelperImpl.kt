package com.example.apicalldemo.api

import com.example.apicalldemo.ResponseClass
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService):ApiHelper {
    override suspend fun getColorList(): Response<ResponseClass> = apiService.getColorsList()

}