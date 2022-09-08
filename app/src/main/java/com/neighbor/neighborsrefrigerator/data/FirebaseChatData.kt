package com.neighbor.neighborsrefrigerator.data

data class ChatMessageData(
    val content: String,
    val is_read: Boolean,
    val createdAt: Long,
    val from: Int
)

data class ChatUserData(
    val id: Int?,
    var nickname: String,
    val level: Int
)

data class FirebaseChatData(
    val id: String?,
    val postId: Int,
    val writer: ChatUserData,
    val contact: ChatUserData,
    val messages: List<ChatMessageData>
)