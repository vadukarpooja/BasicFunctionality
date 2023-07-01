package com.example.apicalldemo.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicalldemo.Resource
import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private val _res = MutableLiveData<Resource<ResponseClass>>()
    private val allNotes = userRepo.getAllNotes()


    val res: LiveData<Resource<ResponseClass>>
        get() = _res


     fun getColorList() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        val res = userRepo.getColorList()
        res.let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }


    fun insertColorList(colorList:ColorsModel){
        userRepo.insert(colorList)
    }

    fun getAllNotes() :LiveData<List<ColorsModel>>{
        return allNotes
    }

}

