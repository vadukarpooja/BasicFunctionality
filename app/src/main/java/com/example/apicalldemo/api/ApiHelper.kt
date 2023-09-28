package com.example.apicalldemo.api

import com.example.apicalldemo.utils.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.IssuesModel
import retrofit2.Response


interface ApiHelper {
    suspend fun getColorList():Response<ResponseClass>

    suspend fun getMoviesList():Response<ArrayList<ColorsModel>>

    suspend fun getIssuesList(perPage: Int, page: Int):Response<ArrayList<IssuesModel>>
/*
suspend fun getIssuesList(perPage: Int, page: Int):Response<ResponseClass>
*/
}