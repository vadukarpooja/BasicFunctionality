package com.example.apicalldemo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicalldemo.Resource
import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.api.RetrofitInstance
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.ColorsModel1
import com.example.apicalldemo.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private val _res = MutableLiveData<Resource<ResponseClass>>()
    var list = MutableLiveData<List<ColorsModel>>()
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
    fun getPopularColor() {
        RetrofitInstance.api.getMovieList().enqueue(object : Callback<ResponseClass>{
            override fun onResponse(call: Call<ResponseClass>, response: Response<ResponseClass>) {
              if(response.isSuccessful){
                  if (response.body()!=null){
                      list.value = response.body()!!.data
                      Log.e(javaClass.simpleName, "onResponse: "+response.body()!!.data)
                  }

               }
            }
            override fun onFailure(call: Call<ResponseClass>, t: Throwable) {
                Log.e(javaClass.simpleName,t.message.toString())
            }
        })
    }

    fun observeColorLiveData() : MutableLiveData<List<ColorsModel>> {
        return list
    }
}

