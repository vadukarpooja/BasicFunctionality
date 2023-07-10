package com.example.apicalldemo.models

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.Provides


@Entity(tableName = "colors_model")
data class ColorsModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val id:String= "",
    @SerializedName("name")
    @Expose
    val name:String= "",
    @SerializedName("year")
    @Expose
    val year:String,
    @SerializedName("color")
    @Expose
    val color:String)
