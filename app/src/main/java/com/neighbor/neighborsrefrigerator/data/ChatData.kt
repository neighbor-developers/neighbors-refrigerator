package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName

data class ChatData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("post_id")
    val postId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class RdbChatData(
    val id: String?,
    val postId: Int,
    val writer: Map<String, RdbUserData>,
    val contact: Map<String, RdbUserData>,
    val messages: ArrayList<RdbMessageData>
)