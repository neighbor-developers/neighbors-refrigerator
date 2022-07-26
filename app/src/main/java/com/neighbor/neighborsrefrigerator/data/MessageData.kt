package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class MessageData(
    @SerializedName("chat_id")
    val chatID: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("message")
    val message: String
)
