package com.example.apicalldemo

import android.content.Context
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader


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
