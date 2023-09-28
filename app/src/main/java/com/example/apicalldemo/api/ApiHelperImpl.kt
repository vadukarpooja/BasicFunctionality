package com.example.apicalldemo.api

import com.example.apicalldemo.utils.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.IssuesModel
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService):ApiHelper {
    override suspend fun getColorList(): Response<ResponseClass> = apiService.getColorsList()
    override suspend fun getMoviesList(): Response<ArrayList<ColorsModel>> = apiService.getMoviesList()
    override suspend fun getIssuesList(
        perPage: Int,
        page: Int
    ): Response<ArrayList<IssuesModel>> = apiService.getIssues(perPage,page)

    /*override suspend fun getIssuesList(
        perPage: Int,
        page: Int
    ): Response<ResponseClass> = apiService.getIssues(perPage,page)*/

}