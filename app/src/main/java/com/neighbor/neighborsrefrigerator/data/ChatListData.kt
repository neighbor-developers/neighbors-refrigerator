package com.neighbor.neighborsrefrigerator.data

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ChatListData(
    @Embedded(prefix = "chatlistdata_") var chatData: Chat?,
    @PrimaryKey(autoGenerate = true) var chatId: String
)

@Entity
data class Chat(
    var postId: String,
    @Embedded(prefix = "writer_") var writer: Inform,
    @Embedded(prefix = "contact_") var contact: Inform,
    // room db에는 기본 타입만 저장 가능 -> @TypeConverter를 통해 저장할 때 json 형식 string 타입으로 저장되고 불러올 때는 리스트로 변환되어 불러와지게 함
    var message: List<ChatMessage>
)

@Entity
data class Inform(
    var image: Bitmap?,
    var id: String,
    var nickname: String,
    var level: Int
)

@Entity
data class ChatMessage(
    var content: String,
    var isRead: Boolean,
    var created_at: String,
    var from: Int
)