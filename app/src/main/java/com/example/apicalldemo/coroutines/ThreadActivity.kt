package com.example.apicalldemo.coroutines

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apicalldemo.adapter.ColorListAdapter
import com.example.apicalldemo.databinding.ActivityThreadBinding
import com.example.apicalldemo.models.ColorsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@AndroidEntryPoint
class ThreadActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    lateinit var binding: ActivityThreadBinding
    val execute = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: ColorListAdapter
    val list: ArrayList<ColorsModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)
      /*  binding.title.setOnClickListener {
            val intent = Intent(this, ReadContactList::class.java)
            startActivity(intent)
        }*/
        adapter = ColorListAdapter(arrayListOf(),{

        },{

        }) {
        }

        // SomeTask(binding.edt).execute(100)
        doWork(binding)
        main()
        handler()
        runOnUi()

    }

    private fun handler() {
        execute.execute {
            val result = "hello"
            handler.post {
                // binding.title.text = result
            }
        }


    }

    private fun runOnUi() {
        Thread(
            Runnable {
                val result = "Hello"
                runOnUiThread {
                    //  binding.edt.setText(result)
                }
            }).start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun main() {
        val value = 0
        val list1: ArrayList<ColorsModel> = arrayListOf()
        /*val list2: ArrayList<ColorsModel> = arrayListOf()*/
        CoroutineScope(Dispatchers.Main).launch {
            val result = 10
            delay(1000L)
            for (i in value+1..result) {

                list1.addAll(listOf(ColorsModel(name = i.toString())))

                Log.e(javaClass.simpleName, "main: $value")
                Log.e(javaClass.simpleName, "Main2:${Thread.currentThread().name + value} ")
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)
            for (i in 11..20) {

                list1.addAll(listOf(ColorsModel(name = i.toString())))

                Log.e(javaClass.simpleName, "main: $value")
                Log.e(
                    javaClass.simpleName, "Main2:${
                        Thread.currentThread().name
                                + value
                    } "
                )
            }
           /* adapter = ColorListAdapter(list1) {
            }
            binding.rvEmployees.adapter = adapter*/
            adapter.notifyDataSetChanged()
          /*  runOnUiThread {
                adapter = ColorListAdapter(list2) {
                }
                binding.rvEmployees2.adapter = adapter
                adapter.notifyDataSetChanged()
            }*/
            /* Toast.makeText(
                 applicationContext,
                 "Hello :${Thread.currentThread().name}",
                 Toast.LENGTH_SHORT
             ).show()*/
        }

    }

    private fun doWork(binding: ActivityThreadBinding) {
        lifecycleScope.launch {
            Log.e(javaClass.simpleName, "doWork task: " + Thread.currentThread().name)
            val result = "Hello Welcome"
            withContext(Dispatchers.Main) {
                Log.e(javaClass.simpleName, "doWork main uI on: " + Thread.currentThread().name)
                //binding.title.text = result
            }
        }

        Log.e(javaClass.simpleName, "doWork end on: " + Thread.currentThread().name)
    }

}

@SuppressLint("StaticFieldLeak")
class SomeTask(private var textView: EditText) : AsyncTask<Int, Void, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
        Log.e(javaClass.simpleName, "onPreExecute: ")
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Int?): String {
        Log.e(javaClass.simpleName, "doInBackground: " + params[0].toString())
        return params[0].toString()

    }


    @Deprecated("Deprecated in Java", ReplaceWith("textView.text = result"))
    override fun onPostExecute(result: String?) {
        textView.setText(result)
        Log.e(javaClass.simpleName, "onPostExecute: $result")
    }
}


@SuppressLint("StaticFieldLeak")
internal class SomeTask1(val textView: TextView,val context: Context) : AsyncTask<String, Void, String>() {
    val handler = Handler()
    val handler2 = Handler()
    override fun doInBackground(vararg strings: String): String {
        return null.toString()
    }
    override fun onPostExecute(s: String) {
        var countUpdate = 1
        textView.text = countUpdate.toString()
        handler.postDelayed(object : java.lang.Runnable {
            override fun run() {
                handler.postDelayed(this, 5000)
                textView.text = countUpdate++.toString()
            }
        }, 4000)
        handler2.postDelayed(object : java.lang.Runnable {
            override fun run() {
                handler2.postDelayed(this, 50000)
                Toast.makeText(context,textView.text.toString(),Toast.LENGTH_SHORT).show()
            }
        }, 50000)
    }
}
