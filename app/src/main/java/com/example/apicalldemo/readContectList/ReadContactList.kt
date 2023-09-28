package com.example.apicalldemo.readContectList

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.ActivityReadContectListBinding
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi

@AndroidEntryPoint
class ReadContactList : AppCompatActivity() {
    val REQUEST_READ_CONTACTS = 79
    private val viewModel: MainViewModel by viewModels()
    var mobileArray: ArrayList<ColorsModel> = arrayListOf()
    private lateinit var adapter: ColorListAdapter
    lateinit var binding: ActivityReadContectListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadContectListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ColorListAdapter(arrayListOf(),{

        },{

        }) {
        }
        loadContacts()

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            Log.e(javaClass.simpleName, "onCreate: "+viewModel.contactsList())
            viewModel.contactsList()
        } else {
            requestPermission()
        }
    }
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            viewModel.contactsList()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            viewModel.contactsList()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        }

    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun loadContacts() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
           /* adapter = ColorListAdapter(getContacts()) {
                Log.e(javaClass.simpleName, "onViewCreated: "+getContacts())
            }
            binding.rvEmployees.adapter = adapter*/
         viewModel.contactsLiveData.observe(this, Observer { it1 ->
              /* adapter = ColorListAdapter(it1) {
                   Log.e(javaClass.simpleName, "onViewCreated: $it1")
               }
               binding.rvEmployees.adapter = adapter*/
           })

        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_CONTACTS), REQUEST_READ_CONTACTS)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.contactsList()
            } else {
                Toast.makeText(applicationContext,"Permission must be granted in order to display contacts information",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    private fun getContacts(): ArrayList<ColorsModel> {
        val contacts = ArrayList<ColorsModel>()
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if (cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    contacts.add(ColorsModel(name=name, year = phoneNumValue))
                                }
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            } else {
                Toast.makeText(applicationContext,"No contacts available!",Toast.LENGTH_SHORT).show()
            }
        }
        cursor?.close()
        return contacts
    }



}