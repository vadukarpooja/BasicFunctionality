package com.example.apicalldemo.api

import com.example.apicalldemo.utils.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.IssuesModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("unknown")
    suspend fun getColorsList(): Response<ResponseClass>

    @GET("unknown")
    fun getMovieList(): Call<ResponseClass>

    /* @GET("db")*/
    @GET("movies")
    suspend fun getMoviesList(): Response<ArrayList<ColorsModel>>

    @GET("issues")
    suspend fun getIssues(@Query("per_page")
                  perPage: Int,
                  @Query("page")
                  page: Int):Response<ArrayList<IssuesModel>>

    /*@GET("users")
    suspend fun getIssues(@Query("per_page")
                          perPage: Int,
                          @Query("page")
                          page: Int):Response<ResponseClass>*/
}
