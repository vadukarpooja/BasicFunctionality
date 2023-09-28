package com.example.apicalldemo.utils

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apicalldemo.models.ColorsModel
import com.example.apicalldemo.models.IssuesModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*@Entity(tableName = "colors_model")*/
data class ResponseClass(
    val page: Int = 0,
    @SerializedName("per_page")
    @Expose
    val perPage: Int = 0,
    @SerializedName("total")
    @Expose
    val total: Int = 0,
    @SerializedName("total_pages")
    @Expose val totalPages: Int =0,
    var data: ArrayList<ColorsModel> = arrayListOf()
)

