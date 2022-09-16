package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName

data class ChatData(
    @SerializedName("id")
    val id: String?,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("contactId")
    val contactId: Int,
    @SerializedName("createdAt")
    val createdAt: String
)
