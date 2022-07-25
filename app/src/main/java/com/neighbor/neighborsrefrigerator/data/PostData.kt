package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class PostData(
    val id: Int,
    val title: String?,
    val categoryId: String,
    val userId: Int,
    val content: String?,
    val type: Int,
    val mainAddr: String,
    val addrDetail: String,
    val rate: Int?,
    val review: String?,
    val validateType: Int,
    val validateDate: String,
    val validateImg: String?,
    val productImg: String,
    val createdAt: String,
    val updatedAt: String?,
    val completedAt: String?,
    val latitude : Double,
    val longitude : Double,
    val state : String,
    val distance : Double
)
