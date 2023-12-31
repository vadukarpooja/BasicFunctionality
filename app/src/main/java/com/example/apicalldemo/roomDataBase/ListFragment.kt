package com.example.apicalldemo.roomDataBase


import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import com.example.apicalldemo.backgrounCountUpdate.service.CountInterFace
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.FragmentListBinding
import com.example.apicalldemo.models.ResponseItem
import com.example.apicalldemo.netWork.NetworkHelper
import com.example.apicalldemo.pref.SharedPrefs
import com.example.apicalldemo.backgrounCountUpdate.service.ForegroundService
import com.example.apicalldemo.utils.BROADCAST
import com.example.apicalldemo.viewModel.MovieListViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


@AndroidEntryPoint
class ListFragment : Fragment() , CountInterFace {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: ColorListAdapter
    private val args: ListFragmentArgs by navArgs()
    private val movieVM: MovieListViewModel by viewModels()
    private lateinit var networkHelper: NetworkHelper
    lateinit var foregroundService: ForegroundService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkHelper = NetworkHelper(requireContext())
        foregroundService = ForegroundService()
        foregroundService.localBroadcastManager =  LocalBroadcastManager.getInstance(requireContext())
        binding.stop.setOnClickListener {
            SharedPrefs.remove(requireContext(),"count")
            SharedPrefs.remove(requireContext(),"result")
        }
        requireContext().startService(Intent(requireContext(), ForegroundService::class.java))
        val listener = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val data = intent?.getStringExtra("count")
                binding.count.text = data
                Log.e(javaClass.simpleName, "onViewCreated onReceive: $data")
            }
        }
        val intentFilter = IntentFilter(BROADCAST)
        foregroundService.localBroadcastManager.registerReceiver(listener, intentFilter)
        /* SomeTask(binding.count, requireContext(), handler, handler2).execute() as SomeTask*/
        adapter = ColorListAdapter(arrayListOf(), {

        }, {

        }) {

        }
        offLineList()
        /* roomDataBase()*/

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

    override fun onUpdateCount(count: Int,localBroadcastManager:LocalBroadcastManager,context: Context){
        val intent = Intent(BROADCAST)
        intent.putExtra("count", count.toString())
        localBroadcastManager.sendBroadcast(intent)
        Log.e(javaClass.simpleName, "onUpdateCount1: $count")
    }
    override fun onAttach(context: Context) {
        Log.e(javaClass.simpleName, "onAttach: ")
        super.onAttach(context)
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
    @Suppress("DEPRECATION")
    private fun <T> Context.isServiceRunning(serviceClass: Class<T>): Boolean {
        var isStart: Boolean = false
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                isStart = true
            }
        }
        return isStart
    }

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
       /* adapter = ColorListAdapter(data, onEditClick = {
        }, onDeleteClick = {
        }, onItem = {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToMovieListFragment(
                    data = it
                )
            )
        })*/
        binding.rvEmployees.adapter = adapter
    }


    private fun roomDataBase() {
        movieVM.getAllMovieItem().observe(viewLifecycleOwner) { it1 ->
            Log.e(javaClass.simpleName, "roomDataBase: $it1")
            /*adapter = ColorListAdapter(it1, onEditClick = { itEdit ->
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToMovieItemEdit(itEdit)
                )
            }, onDeleteClick = { itDelete ->
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Are You Sure want to deleteItem")
                builder.setPositiveButton("Yes") { _, _ ->
                    val responseItem1 = ResponseItem(
                        id = itDelete.id,
                        released = itDelete.released,
                        director = itDelete.director,
                        title = itDelete.title,
                        actors = itDelete.director,
                        country = itDelete.country,
                        poster = itDelete.poster
                    )
                    movieVM.deleteMovieItem(responseItem1)
                    Toast.makeText(requireContext(), "Movie Item is deleted", Toast.LENGTH_SHORT)
                        .show()
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }, onItem = { item ->
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToMovieListFragment(
                        item
                    )
                )
            })*/
            binding.rvEmployees.adapter = adapter

        }

        binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Are You Sure want to deleteItem")
            builder.setPositiveButton("Yes") { _, _ ->
                movieVM.deleteAllMovieItem()
                Toast.makeText(requireContext(), "All Item is deleted", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

        }
    }

    @SuppressLint("StaticFieldLeak")
    class SomeTask(
        val textView: TextView,
        val context: Context,
        val handler: Handler,
        val handler2: Handler,
    ) : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg strings: String): String {
            return null.toString()
        }

        override fun onPostExecute(s: String) {
            var countUpdate = 1
            textView.text = countUpdate.toString()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    handler.postDelayed(this, 5000)
                    textView.text = countUpdate++.toString()
                }
            }, 4000)
            handler2.postDelayed(object : Runnable {
                override fun run() {
                    handler2.postDelayed(this, 50000)
                    Toast.makeText(context, textView.text.toString(), Toast.LENGTH_SHORT).show()
                }
            }, 50000)
        }
    }




    var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //myService = (service as ForegroundService.MyBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e(javaClass.simpleName, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Log.e(javaClass.simpleName, "onResume: ")
    }

    override fun onPause() {
        /*  requireContext().stopService(serviceIntent)
          requireContext().unbindService(serviceConnection)*/
        super.onPause()
        Log.e(javaClass.simpleName, "onPause: ")
    }


    companion object {
        const val MESSENGER_INTENT_KEY = "msg-intent-key"
    }

    @SuppressLint("HandlerLeak")
    inner class IncomingMessageHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
               /* ForegroundService().LOCATION_MESSAGE -> {
                    Log.e(javaClass.simpleName, "handleMessage: ${msg.obj}")
                    binding.count.text = msg.obj.toString()
                }*/
                }
            }
        }

    interface CountResult {
        fun resultCount(count: String){}
    }
}
