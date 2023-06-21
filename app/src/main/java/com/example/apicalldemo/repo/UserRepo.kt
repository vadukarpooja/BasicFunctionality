package com.example.apicalldemo.repo


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.ColorDao
import com.example.apicalldemo.ColorDataBase
import com.example.apicalldemo.Resource
import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.api.ApiHelper
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.subscribeOnBackground
import javax.inject.Inject

class UserRepo @Inject constructor(private val apiHelper: ApiHelper, application: BaseApplication) {


    suspend fun getColorList() = apiHelper.getColorList()

    private  var colorDao: ColorDao
    private  var allColorList: LiveData<List<ColorsModel>>
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

    fun getAllNotes(): LiveData<List<ColorsModel>> {
        return allColorList
    }
}