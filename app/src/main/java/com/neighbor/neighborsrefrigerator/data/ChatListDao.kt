package com.neighbor.neighborsrefrigerator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatListDao {

    @Query("SELECT * FROM ChatData")
    fun getChatMessage(): List<ChatData>

    @Insert
    fun insert(chatList: ChatData)

/*    @Query("Select *")
    fun getInfo(): List<ChatData>*/

}
