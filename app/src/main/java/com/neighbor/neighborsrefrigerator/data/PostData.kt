package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class PostData(
    val id: Int,
    val title: String?,
    val categoryId: String,
    val userId: Int,
    val content: String?,
    val postType: Int,
    val postAddr: String,
    val rate: Double?,
    val review: String?,
    val productName: String,
    val validateType: Int,
    val validateDate: Date,
    val validateImg: String?,
    val productImg: String,
    val createdAt: Date,
    val updatedAt: Date?,
    val completedAt: Date?,
    val state : String
)
