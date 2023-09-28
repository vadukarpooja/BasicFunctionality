package com.example.apicalldemo.repo

import androidx.lifecycle.LiveData
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.roomDataBase.ColorDao
import com.example.apicalldemo.roomDataBase.ColorDataBase
import com.example.apicalldemo.roomDataBase.MovieListDao
import com.example.apicalldemo.roomDataBase.MovieListDatabase
import com.example.apicalldemo.api.ApiHelper
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ResponseItem
import com.example.apicalldemo.utils.subscribeOnBackground
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


    private var movieListDao: MovieListDao
    private var movieItem1: LiveData<List<ResponseItem>>
    private val database1 = MovieListDatabase.getInstance(application.applicationContext)


    init {
        movieListDao = database1.movieListDao()
        movieItem1 = movieListDao.getAllMovies()
    }

    fun insert(item: ResponseItem) {
        subscribeOnBackground {
            movieListDao.insert(item)
        }
    }

    fun update(item:ResponseItem){
        subscribeOnBackground {
            movieListDao.update(item)
        }
    }

    fun delete(item:ResponseItem){
        subscribeOnBackground {
            movieListDao.delete(item)
        }
    }

    fun deleteAll(){
        subscribeOnBackground {
            movieListDao.deleteMovieItem()
        }
    }

    fun getMovieItem(): LiveData<List<ResponseItem>> {
        return movieItem1
    }
}