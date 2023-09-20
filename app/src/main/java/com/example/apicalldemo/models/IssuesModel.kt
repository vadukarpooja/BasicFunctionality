package com.example.apicalldemo.models

import com.google.gson.annotations.SerializedName

data class IssuesModel(
   /* @SerializedName("number")
    var number: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("user")
    var user: User = User(),*/
    @SerializedName("id")
    var id:String ,
    @SerializedName("email")
    var email: String = ""
)

data class User(
    @SerializedName("login")
    var login: String = ""
)