package com.example.apicalldemo.utils

import android.content.Context
import android.util.Log
import android.view.View
import com.example.apicalldemo.models.IssuesModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random


fun subscribeOnBackground(function: () -> Unit) {
    Single.fromCallable {
        function()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
}

fun readJSONFromAssets(context: Context, path: String): String {
    val identifier = "[ReadJSON]"
    return try {
        val file = context.assets.open("$path")

        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
fun View.onClick(action: () -> Unit) {
    setOnClickListener { action() }
}

fun list():ArrayList<IssuesModel>{
    val list:ArrayList<IssuesModel> = arrayListOf()
    list.add(IssuesModel(number = 1))
    list.add(IssuesModel(number = 2))
    list.add(IssuesModel(number = 3))
    list.add(IssuesModel(number = 4))
    list.add(IssuesModel(number = 5))
    list.add(IssuesModel(number = 6))
    list.add(IssuesModel(number = 7))
    list.add(IssuesModel(number = 8))
    list.add(IssuesModel(number = 9))
    return  list

}

fun randomUnrepeated(from: Int, to: Int):MutableList<Int> {
     val numbers = (from..to).toMutableList()
    return numbers
}

fun nextInt(from: Int, to: Int): Int {
    val index = kotlin.random.Random.nextInt(randomUnrepeated(from,to).size)
    val number = randomUnrepeated(from, to)[index]
    randomUnrepeated(from, to).removeAt(index)
    return number
}


