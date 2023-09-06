package com.example.apicalldemo.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


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
    val year:String = "",
    @SerializedName("color")
    @Expose
    val color:String = "",)



@Parcelize
data class MovieList(
    @SerializedName("Title")
    var title:String = "",
    @SerializedName("Poster")
    var poster:String = "",
    @SerializedName("Writer")
    var writer:String = " ",
    @SerializedName("Actors")
    var actors:String = "",
    @SerializedName("Country")
    var country:String = "",
    @SerializedName("Type")
    var type:String = "",
    @SerializedName("Director")
    var director:String = "",
    @SerializedName("Released")
    var released:String = ""):Parcelable {
}



data class Images(
    var img:String = ""
)/*{
    override fun toString(): String {
        return "Images(data='$data')"
    }
}*/

