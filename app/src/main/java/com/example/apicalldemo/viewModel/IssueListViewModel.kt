package com.example.apicalldemo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicalldemo.Resource
import com.example.apicalldemo.ResponseClass
import com.example.apicalldemo.models.IssuesModel
import com.example.apicalldemo.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IssueListViewModel @Inject constructor(private val userRepo: UserRepo):ViewModel(){
    private val _res = MutableLiveData<Resource<ArrayList<IssuesModel>>>()
    val res: LiveData<Resource<ArrayList<IssuesModel>>>
        get() = _res

    fun getIssueList(perPage:Int,page:Int) = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        val res = userRepo.getIssuesList(perPage, page)
        res.let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }
}