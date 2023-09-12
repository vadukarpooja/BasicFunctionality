package com.example.apicalldemo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.ColorDao
import com.example.apicalldemo.ColorDataBase
import com.example.apicalldemo.MovieListDao
import com.example.apicalldemo.MovieListDatabase
import com.example.apicalldemo.Resource
import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ResponseItem
import com.example.apicalldemo.repo.MovieListRepo
import com.example.apicalldemo.repo.UserRepo
import com.example.apicalldemo.subscribeOnBackground
import com.google.gson.JsonArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepo: MovieListRepo,
    val baseApplication: BaseApplication
) : ViewModel() {
    private val _res = MutableLiveData<Resource<ArrayList<ColorsModel>>>()
    private val allMovieList = movieListRepo.getMovieList()
    private val movieItem = movieListRepo.getMovieItem()
    val res: LiveData<Resource<ArrayList<ColorsModel>>>
        get() = _res

    fun getMovieList() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        val res = movieListRepo.getMoviesList()

        res.let {
            if (it.isSuccessful) {
                Log.e(javaClass.simpleName, "getMovieList: ${it.body()}")
                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

   /* fun insertColorList(colorList: ColorsModel) {
        movieListRepo.insert(colorList)
    }

    fun getAllMovieList(): LiveData<List<ColorsModel>> {
        return allMovieList
    }
*/

    fun insertMovieList(responseItem:ResponseItem) {
        movieListRepo.insert(responseItem)
    }

    fun updateMovieList(responseItem: ResponseItem){
        movieListRepo.update(responseItem)
    }
    fun getAllMovieItem() :LiveData<List<ResponseItem>>{
        return movieItem
    }

    fun deleteMovieItem(responseItem: ResponseItem){
        movieListRepo.delete(responseItem)
    }

    fun deleteAllMovieItem()= viewModelScope.launch{
        movieListRepo.deleteAll()
    }

}