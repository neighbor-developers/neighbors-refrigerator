package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName

data class ReturnObjectForPost (
        @SerializedName("isConnect")
        val isConnect: Boolean,
        @SerializedName("resultCode")
        val resultCode: Int,
        @SerializedName("msg")
        val msg: String,
        @SerializedName("result")
        val result: ArrayList<PostData>
)