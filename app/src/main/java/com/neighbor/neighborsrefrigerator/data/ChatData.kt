package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class ChatData(
    val postID: Int,
    val contactUID: Int,
    val chatID: String,
    val charLog: Date,
    val updatedAt: Date,
    val char: String,
    val chatFrom: Int
)
