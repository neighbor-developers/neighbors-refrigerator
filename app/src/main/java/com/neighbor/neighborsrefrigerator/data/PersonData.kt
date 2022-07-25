package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class PersonData(
    val id: Int,
    val fbID: String,
    val email: String,
    val nickname: String,
    val homeAddr: String,
    val reportPoint: Int = 0,
    val latitude : Double,
    val longitude : Double,
    val createdAt: Date
)
