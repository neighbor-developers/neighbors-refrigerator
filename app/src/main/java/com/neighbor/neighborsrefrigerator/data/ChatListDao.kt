package com.neighbor.neighborsrefrigerator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatListDao {
    @Query("SELECT * FROM ChatList WHERE id = :id")
    fun getChatMessage(id: String): List<ChatMessageData>

    @Insert
    fun insert(chatList: ChatList)

    @Query("DELETE FROM ChatList WHERE id = :id")
    fun deleteChatList(id: String)
}