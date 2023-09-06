package com.example.apicalldemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.FragmentColorListBinding
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.viewModel.MainViewModel
import com.example.apicalldemo.viewModel.MovieListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class ColorListFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ColorListAdapter
    lateinit var binding: FragmentColorListBinding
    private lateinit var networkHelper: NetworkHelper
    var url:String ="https://reqres.in/api/unknown"


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
        adapter = ColorListAdapter(arrayListOf()) {
        }
        /*res.data?.let { it1 ->
            adapter = ColorListAdapter(it1)
        }*/

        if (networkHelper.isNetworkConnected()) {
            Log.e(javaClass.simpleName, "onLine: ")
           // viewModel.getPopularColor()
            //getListUsingOlyMvvm(this)
            viewModel.getColorList()
            //volleyApiList()
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
                            /*adapter = ColorListAdapter(res) {
                                findNavController().navigate(ColorListFragmentDirections.actionColorListFragmentToMapFragment2())
                            }
                            binding.rvEmployees.adapter = adapter*/
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




    private fun  offLineList() {
        viewModel.getAllNotes().observe(viewLifecycleOwner) {
            /*adapter = ColorListAdapter(it as ArrayList<ColorsModel>) {
                findNavController().navigate(ColorListFragmentDirections.actionColorListFragmentToMapFragment2())
            }
            binding.rvEmployees.adapter = adapter*/
        }

    }

    private fun volleyApiList(){
        val list = ArrayList<ColorsModel>()
        val queue:RequestQueue = Volley.newRequestQueue(requireContext())
        val request: StringRequest =
            object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val data: JSONArray = jsonObject.getJSONArray("data")
                        Log.e(javaClass.simpleName, "volleyApiList: $data")
                        for (i in 0 until data.length()) {
                            val tutorialsObject: JSONObject = data.getJSONObject(i)

                            val colorList = ColorsModel(
                                tutorialsObject.getString("id"),
                                tutorialsObject.getString("name"),
                                tutorialsObject.getString("year"),
                                tutorialsObject.getString("color"))
                            list.add(colorList)

                        }
                        list.forEach { it1 ->
                            viewModel.insertColorList(it1)
                            Log.e(javaClass.simpleName, "insertColorList: $it1")
                        }
                     /*   adapter = ColorListAdapter(list) {
                            findNavController().navigate(ColorListFragmentDirections.actionColorListFragmentToMapFragment2())
                        }
                        binding.rvEmployees.adapter = adapter*/

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    Log.e(javaClass.simpleName, "error is " + error.message)
                }) {
            }
        queue.add(request)
    }

    private fun getListUsingOlyMvvm(lifecycleOwner: LifecycleOwner){
        viewModel.observeColorLiveData().observe(lifecycleOwner) {
           /* adapter = ColorListAdapter(it) {
                findNavController().navigate(ColorListFragmentDirections.actionColorListFragmentToMapFragment2())
            }
            binding.rvEmployees.adapter = adapter*/
        }
    }

}


