package com.neighbor.neighborsrefrigerator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ChatList(    @PrimaryKey(autoGenerate = true)
                        val id: Int = 0,

                        val postId: Int,
    val contactId: Int,
    val message: String,
)