package com.example.apicalldemo.models

import android.os.Parcelable
import androidx.databinding.adapters.Converters
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Type


@Parcelize
@Entity(tableName = "movieList_model")
data class ResponseItem(
	@PrimaryKey(autoGenerate = true)
	@SerializedName("id")
	var id:Int = 0,
	@SerializedName("Released")
	var released: String = "",
	/*@SerializedName("Images")
	@TypeConverters(ConvertersImageList::class)
	var images: List<String> = ArrayList(),*/
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

): Parcelable

object ConvertersImageList {
	@TypeConverter
	fun fromString(value: String?): ArrayList<String> {
		val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
		return Gson().fromJson(value, listType)
	}

	@TypeConverter
	fun fromArrayList(list: ArrayList<String?>?): String {
		val gson = Gson()
		return gson.toJson(list)
	}
}
