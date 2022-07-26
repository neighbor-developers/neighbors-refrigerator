package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class ChatData(
    @SerializedName("id")
    val id: String,
    @SerializedName("post_id")
    val postId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)
