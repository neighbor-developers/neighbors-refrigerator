package com.neighbor.neighborsrefrigerator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatList::class], version = 1)
abstract class ChatListDatabase: RoomDatabase() {
    abstract fun chatListDao(): ChatListDao

    companion object {
        private var instance: ChatListDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ChatListDatabase?
        {
            if(instance == null){
                synchronized(ChatListDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatListDatabase::class.java,
                        "chatList-database"
                    ).build()
                }
            }
            return instance
        }
    }
}