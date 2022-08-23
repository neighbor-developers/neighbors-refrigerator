package com.neighbor.neighborsrefrigerator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatListDao {

    @Query("SELECT * FROM ChatListData.chat WHERE writer_id = :id || contact_id = :id")
    fun getChatMessage(id: String): List<Chat>

    @Insert
    fun insert(chatList: ChatData)

/*    @Query("DELETE FROM ChatListData WHERE p = :id")
    fun deleteChatList(id: String)*/

}
