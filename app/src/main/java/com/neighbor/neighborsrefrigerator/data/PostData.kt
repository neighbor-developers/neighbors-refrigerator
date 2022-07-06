package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class PostData(
    val postID: Int,
    val title: String?,
    val category: String,
    val uId: Int,
    val content: String?,
    val postType: Int,
    val postAddr: String,
    val rate: Double?,
    val review: String?,
    val createdAt: Date,
    val updatedAt: Date?,
    val completedAt: Date?
)
