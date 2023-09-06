package com.example.apicalldemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.ActivityListUpdateBinding
import com.example.apicalldemo.models.ColorsModel
import com.google.gson.JsonObject

class ListUpdate : AppCompatActivity() {
    private lateinit var binding: ActivityListUpdateBinding
    private lateinit var adapter: ColorListAdapter
    val list:ArrayList<ColorsModel> = arrayListOf()
    var data:String=" "
    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ColorListAdapter(arrayListOf()) {
        }

        binding.addBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.edtAdd.text)) {
                Toast.makeText(applicationContext, "Enter Add Number", Toast.LENGTH_SHORT).show()
            }
            else {
                val dupValue = list.count { it.name == binding.edtAdd.text.toString() }
                Log.e(javaClass.simpleName, "dupValue: $dupValue")
                if (dupValue == 1){
                    Toast.makeText(applicationContext,"value is already added",Toast.LENGTH_SHORT).show()
                }
                else{
                    data = binding.edtAdd.text.toString()
                    list.addAll(listOf(ColorsModel(name = data)))
                   /* adapter = ColorListAdapter(list) {
                    }
                    binding.rvEmployees.adapter = adapter*/
                    Log.e(javaClass.simpleName, "add List: $list")
                }
                binding.edtAdd.text?.clear()
            }
        }
        binding.btnDelete.setOnClickListener {
            if (TextUtils.isEmpty(binding.edtDelete.text)){
                Toast.makeText(applicationContext,"Enter delete number",Toast.LENGTH_SHORT).show()
            }
            else if(list.isEmpty()){
                Toast.makeText(applicationContext,"No Data Available",Toast.LENGTH_SHORT).show()
            }else{
                var value:ColorsModel = ColorsModel()
                for (i in list) {
                        if (i.name.contains(binding.edtDelete.text.toString())){
                            list.remove(i)
                            value = i
                            Log.e(javaClass.simpleName, "remove list: $list")
                            Toast.makeText(applicationContext,"Number ${i.name} is remove",Toast.LENGTH_SHORT).show()
                            adapter.notifyDataSetChanged()
                            break
                        }
                    }
                 if (!value.name.contains(binding.edtDelete.text.toString())){
                    Toast.makeText(applicationContext,"this number ${binding.edtDelete.text.toString()} is not added",Toast.LENGTH_SHORT).show()
                }
            }
            binding.edtDelete.text?.clear()
        }
    }
}