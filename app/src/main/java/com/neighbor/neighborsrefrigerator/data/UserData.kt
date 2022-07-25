package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.*

data class UserData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fbID")
    val fbID: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("homeAddr")
    val homeAddr: String,
    @SerializedName("reportPoint")
    val reportPoint: Int = 0,
    @SerializedName("latitude")
    val latitude : Double,
    @SerializedName("longitude")
    val longitude : Double,
    @SerializedName("createdAt")
    val createdAt: String
)
