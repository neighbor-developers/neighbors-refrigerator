package com.neighbor.neighborsrefrigerator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ChatMessageData(
    var content: String,
    var isRead: Boolean,
    var created_at: String,
    var from: Int
)