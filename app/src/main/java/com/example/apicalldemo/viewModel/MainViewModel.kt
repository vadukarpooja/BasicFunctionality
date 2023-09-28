package com.example.apicalldemo.viewModel

import android.annotation.SuppressLint
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apicalldemo.BaseApplication
import com.example.apicalldemo.utils.Resource
import com.example.apicalldemo.utils.ResponseClass
import com.example.apicalldemo.api.RetrofitInstance
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepo: UserRepo,val baseApplication: BaseApplication) : ViewModel() {
    private val _res = MutableLiveData<Resource<ResponseClass>>()
    var list = MutableLiveData<List<ColorsModel>>()
    private val _contactsLiveData = MutableLiveData<ArrayList<ColorsModel>>()
    val contactsLiveData:LiveData<ArrayList<ColorsModel>> = _contactsLiveData
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
                     /* list.value = response.body()!!.data
                      response.body()!!.data.forEach {
                          insertColorList(it)
                      }*/
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

    fun contactsList() {
        viewModelScope.launch {
            val contactsListAsync = async { getContacts() }

            val contacts = contactsListAsync.await()
            _contactsLiveData.postValue(contacts)
        }
    }


    @SuppressLint("Range")
    private fun getContacts(): ArrayList<ColorsModel> {
        val contacts = ArrayList<ColorsModel>()
        val cursor = baseApplication.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = baseApplication.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if (cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    contacts.add(ColorsModel(name=name, year = phoneNumValue))
                                }
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            } else {
                Toast.makeText(baseApplication,"No contacts available!", Toast.LENGTH_SHORT).show()
            }
        }
        cursor?.close()
        return contacts
    }

}

