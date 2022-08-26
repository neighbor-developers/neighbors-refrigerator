package com.neighbor.neighborsrefrigerator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatListDao {

    @Query("SELECT * FROM ChatListData")
    fun getChatMessage(): List<ChatListData>

/*    @Insert
    fun insert(chatList: ChatListData)

    @Query("DELETE FROM ChatListData WHERE p = :id")
    fun deleteChatList(id: String)*/

}
