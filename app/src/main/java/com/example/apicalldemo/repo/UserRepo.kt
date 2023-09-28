package com.example.apicalldemo.repo


import androidx.lifecycle.LiveData
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.roomDataBase.ColorDao
import com.example.apicalldemo.roomDataBase.ColorDataBase
import com.example.apicalldemo.api.ApiHelper
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.IssuesModel
import com.example.apicalldemo.utils.subscribeOnBackground
import retrofit2.Response
import javax.inject.Inject

class UserRepo @Inject constructor(private val apiHelper: ApiHelper, application: BaseApplication) {


    suspend fun getColorList() = apiHelper.getColorList()

    suspend fun getMoviesList() = apiHelper.getMoviesList()

    suspend fun getIssuesList( perPage: Int,
                               page: Int): Response<ArrayList<IssuesModel>> = apiHelper.getIssuesList(perPage, page)

    /*suspend fun getIssuesList( perPage: Int,
                               page: Int): Response<ResponseClass> = apiHelper.getIssuesList(perPage, page)*/

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