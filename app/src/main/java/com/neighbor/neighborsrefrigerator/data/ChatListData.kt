package com.neighbor.neighborsrefrigerator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ChatList(
    val postId: Int,
    val contactId: Int,
    val message: ChatMessageData,
){
    @PrimaryKey(autoGenerate = true) var id: String = "${postId}_${contactId}"
}