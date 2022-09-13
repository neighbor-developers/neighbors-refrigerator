package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName

data class MailData(
    @SerializedName("id")
    val id : Int,
    @SerializedName("postId")
    val postId : Int,
    @SerializedName("content")
    val content : String
)
