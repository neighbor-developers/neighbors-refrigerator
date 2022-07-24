package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class ProductData(
    val id: String,
    val postID: Int,
    val productName: String,
    val validateType: Int,
    val validateDate: Date,
    val validateImg: String?,
    val productImg: String
)
