package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName

data class DataTransferObject<T> (
    @SerializedName("id")
    val id: Int,
    @SerializedName("data")
    val result: T
)
