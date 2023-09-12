package com.example.apicalldemo.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
/*@Serializable
@JsonClass(generateAdapter = true)*/
@Entity(tableName = "movieList_model")
data class ResponseItem(
	@PrimaryKey(autoGenerate = true)
	@SerializedName("id")
	var id:Int = 0,
	@SerializedName("Released")
	var released: String = "",
	/*@SerializedName("Images")
	private var images: List<String>,*/
	@SerializedName("Director")
	var director: String = "",
	@SerializedName("Title")
	var title: String = "",
	@SerializedName("Actors")
	var actors: String = "",
	@SerializedName("Type")
	var type: String = "",
	@SerializedName("Poster")
	var poster: String = "",
	@SerializedName("Country")
	var country: String = "",
):Parcelable

