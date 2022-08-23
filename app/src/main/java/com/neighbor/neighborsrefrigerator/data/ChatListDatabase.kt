package com.neighbor.neighborsrefrigerator.data

import android.content.Context
import androidx.room.*

// https://youngest-programming.tistory.com/456
// https://developer.android.com/training/data-storage/room/relationships?hl=ko


@Database(entities = [ChatData::class], version = 1)
@TypeConverters(MyTypeConverters::class)
abstract class ChatListDB : RoomDatabase() {
    abstract fun chatListDao(): ChatListDao?

    companion object {
        private var INSTANCE: ChatListDB? = null
        fun getInstance(context: Context): ChatListDB? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ChatListDB::class.java,
                    "chatList-database"
                ).build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}