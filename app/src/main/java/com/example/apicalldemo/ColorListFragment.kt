package com.example.apicalldemo

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.FragmentColorListBinding
import com.example.apicalldemo.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ColorListFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ColorListAdapter
    lateinit var binding: FragmentColorListBinding
    lateinit var networkHelper: NetworkHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentColorListBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkHelper = NetworkHelper(requireContext())
        adapter = ColorListAdapter(arrayListOf())
        binding.rvEmployees.adapter = adapter
        /*res.data?.let { it1 ->
            adapter = ColorListAdapter(it1)
        }*/

        if (networkHelper.isNetworkConnected()) {
            Log.e(javaClass.simpleName, "onLine: ")
            viewModel.getColorList()
            onLineList()
        } else {
            offLineList()
            Log.e(javaClass.simpleName, "offline")
        }

    }


    private fun onLineList() {
        viewModel.res.observe(requireActivity(), Observer {
            Log.e(javaClass.simpleName, "observe: $it")
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    binding.rvEmployees.visibility = View.VISIBLE
                    if (it.data != null) {
                        val colorsModel: ResponseClass = it.data
                        colorsModel.data.forEach { it1 ->
                            viewModel.insertColorList(it1)
                            Log.e(javaClass.simpleName, "insertColorList: $it1")
                        }
                        colorsModel.data.let { res ->
                            Log.e(javaClass.simpleName, "onViewCreated: you are online")
                            Log.e(javaClass.simpleName, "res: $res")
                            adapter = ColorListAdapter(res)
                            binding.rvEmployees.adapter = adapter
                        }
                    }
                }

                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.rvEmployees.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    binding.rvEmployees.visibility = View.VISIBLE
                    Log.e(javaClass.simpleName, "Something went wrong: ")
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun offLineList() {
        viewModel.getAllNotes().observe(viewLifecycleOwner) {
            adapter = ColorListAdapter(it)
            binding.rvEmployees.adapter = adapter
        }

    }


}


