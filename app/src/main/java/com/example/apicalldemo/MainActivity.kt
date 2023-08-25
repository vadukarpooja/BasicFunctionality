package com.example.apicalldemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.ActivityMainBinding
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel :MainViewModel by viewModels()
    private lateinit var adapter: ColorListAdapter
    lateinit var car:Car
    lateinit var car2:Car2
    lateinit var engin: Engin


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        car = Car()
        engin = Engin()//
        car2 = Car2(engin)


        car.start("car")
        car2.start("car")

    }



}
