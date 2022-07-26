package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.*

data class UserData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("fb_id")
    val fbID: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("home_addr")
    val homeAddr: String,
    @SerializedName("report_point")
    val reportPoint: Int = 0,
    @SerializedName("latitude")
    val latitude : Double,
    @SerializedName("longitude")
    val longitude : Double,
    @SerializedName("created_at")
    val createdAt: String
)
