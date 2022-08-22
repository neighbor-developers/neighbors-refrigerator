package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class ProductData (
    val id: Int,
    val postId :Int,
    val productName:String,
    val validateType: Int,
    val validateDate: Date,
    val validateImg: String?,
    val productimg1: String
)