package com.neighbor.neighborsrefrigerator.data

data class ChatMessageData(
    val content: String,
    var is_read: Boolean,
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
    var messages: List<ChatMessageData>
)