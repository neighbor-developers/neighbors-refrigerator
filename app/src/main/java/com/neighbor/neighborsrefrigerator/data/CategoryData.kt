package com.neighbor.neighborsrefrigerator.data

import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
