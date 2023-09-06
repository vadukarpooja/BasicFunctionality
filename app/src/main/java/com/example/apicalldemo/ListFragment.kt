package com.example.apicalldemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide.init
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.FragmentListBinding
import com.example.apicalldemo.models.ResponseItem
import com.example.apicalldemo.viewModel.MovieListViewModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


@AndroidEntryPoint
class ListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: ColorListAdapter
    private val args: ListFragmentArgs by navArgs()
    private val movieVM: MovieListViewModel by viewModels()
    private lateinit var networkHelper: NetworkHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkHelper = NetworkHelper(requireContext())
        adapter = ColorListAdapter(arrayListOf()) {

        }
        /*offLineList()*/
        roomDataBase()
        /*if (networkHelper.isNetworkConnected()) {
            Log.e(javaClass.simpleName, "onLine: ")
            movieVM.getMovieList()
            movieList()
        } else {

            Log.e(javaClass.simpleName, "offline")
        }
*/
        Log.e(javaClass.simpleName, "onViewCreated args: ${args.list}")
        /* val gson = Gson()
         val data:ArrayList<ColorsModel>  = gson.fromJson(args.list,object : TypeToken<List<ColorsModel>>() {}.type)
         Log.e(javaClass.simpleName, "onViewCreated data: $data")
           adapter = ColorListAdapter(data) {
           }
           binding.rvEmployees.adapter = adapter*/
    }

    /*  private fun movieList() {
          movieVM.res.observe(requireActivity(), Observer {
              when (it.status) {
                  Status.SUCCESS -> {
                      binding.progress.visibility = View.GONE
                      binding.rvEmployees.visibility = View.VISIBLE
                      if (it.data != null) {
                          val responseClass: ResponseClass = ResponseClass()
                          responseClass.data = it.data
                          responseClass.data.forEach { it1 ->
                              movieVM.insertColorList(it1)
                              Log.e(javaClass.simpleName, "insertColorList: $it1")
                          }
                          val list = it.data
                          adapter = ColorListAdapter(it.data) {
                              findNavController().navigate(ListFragmentDirections.actionListFragmentToMovieListFragment(data = Gson().toJson(list)))
                          }
                          binding.rvEmployees.adapter = adapter

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
  */
    private fun offLineList() {
        val json: InputStream = requireContext().assets.open("film.json")
        val br = BufferedReader(InputStreamReader(json))
        val data: ArrayList<ResponseItem> =
            Gson().fromJson(br, object : TypeToken<List<ResponseItem>>() {}.type)
        data.forEach {
            movieVM.insertMovieList(it)
            Log.e(javaClass.simpleName, "offLineList it: $it")
        }
        Log.e(javaClass.simpleName, "offLineList: $data")
        adapter = ColorListAdapter(data) {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToMovieListFragment(
                    data = it
                )
            )
        }
        binding.rvEmployees.adapter = adapter
    }


    private fun roomDataBase() {
        movieVM.getAllMovieItem().observe(viewLifecycleOwner) { it1 ->
            adapter = ColorListAdapter(it1 as ArrayList<ResponseItem>) {
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToMovieListFragment(
                        data = it
                    )
                )
            }
            binding.rvEmployees.adapter = adapter
        }
    }


}