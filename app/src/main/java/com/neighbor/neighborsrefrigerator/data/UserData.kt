package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserData(
    val id: Int,
    val fbID: String,
    val email: String,
    val nickname: String,
    val homeAddr: String,
    val reportPoint: Int = 0,
    val latitude : Double,
    val longitude : Double,
    val createdAt: String
)
