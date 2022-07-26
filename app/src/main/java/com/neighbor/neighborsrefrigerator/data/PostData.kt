package com.neighbor.neighborsrefrigerator.data

import java.io.Serializable
import com.google.gson.annotations.SerializedName
import java.util.*

data class PostData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("main_addr")
    val mainAddr: String,
    @SerializedName("addr_detail")
    val addrDetail: String,
    @SerializedName("rate")
    val rate: Int?,
    @SerializedName("review")
    val review: String?,
    @SerializedName("validate_type")
    val validateType: Int,
//    @SerializedName("validate_date")
    val validateDate: String?,
    @SerializedName("validate_img")
    val validateImg: String?,
    @SerializedName("product_img")
    val productImg: String?,
//    @SerializedName("created_at")
    val createdAt: String,
//    @SerializedName("updated_at")
    val updatedAt: String?,
//    @SerializedName("completed_at")
    val completedAt: String?,
    @SerializedName("latitude")
    val latitude : Double,
    @SerializedName("longitude")
    val longitude : Double,
    @SerializedName("state")
    val state : String,
    val distance : Double?
): Serializable
