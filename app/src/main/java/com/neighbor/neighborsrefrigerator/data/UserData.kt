package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.*

data class UserData(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("fb_id")
    val fbID: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("home_addr")    //  메인주소랑 상세주소 나누기
    val addressMain: String,
    @SerializedName("addr_detail") //  이름 보준님과 상의 후 결정
    val addressDetail: String,
    @SerializedName("report_point")
    val reportPoint: Int = 0,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("fcm")
    var fcm: String?
)

