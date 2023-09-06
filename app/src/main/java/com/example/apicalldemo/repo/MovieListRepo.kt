package com.example.apicalldemo.repo

import androidx.lifecycle.LiveData
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.ColorDao
import com.example.apicalldemo.ColorDataBase
import com.example.apicalldemo.MovieListDao
import com.example.apicalldemo.MovieListDatabase
import com.example.apicalldemo.api.ApiHelper
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ResponseItem
import com.example.apicalldemo.subscribeOnBackground
import javax.inject.Inject

class MovieListRepo @Inject constructor(private val apiHelper: ApiHelper, application: BaseApplication) {

    suspend fun getMoviesList() = apiHelper.getMoviesList()

    private var colorDao: ColorDao
    private var allColorList: LiveData<List<ColorsModel>>
    private val database = ColorDataBase.getInstance(application.applicationContext)


    init {
        colorDao = database.colorDao()
        allColorList = colorDao.getAllNotes()
    }

    fun insert(color: ColorsModel) {
        subscribeOnBackground {
            colorDao.insert(color)
        }
    }

    fun getMovieList(): LiveData<List<ColorsModel>> {
        return allColorList
    }


    var movieListDao: MovieListDao
    var movieItem1: LiveData<List<ResponseItem>>
    val database1 = MovieListDatabase.getInstance(application.applicationContext)


    init {
        movieListDao = database1.movieListDao()
        movieItem1 = movieListDao.getAllMovies()
    }

    fun insert(item: ResponseItem) {
        subscribeOnBackground {
            movieListDao.insert(item)
        }
    }

    fun getMovieItem(): LiveData<List<ResponseItem>> {
        return movieItem1
    }
}