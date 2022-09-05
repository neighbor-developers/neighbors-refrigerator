package com.neighbor.neighborsrefrigerator.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


//@Entity
//data class ChatData(
//    @Embedded(prefix = "chatlistdb_") var chatData: Chat?,
//    @PrimaryKey(autoGenerate = true) var chatId: Int
//)
//
//
//data class ChatDetail(
//    var postId: String,
//    @Embedded(prefix = "writer_") var writer: ChatUserData,
//    @Embedded(prefix = "contact_") var contact: ChatUserData,
//    // room db에는 기본 타입만 저장 가능 -> @TypeConverter를 통해 저장할 때 json 형식 string 타입으로 저장되고 불러올 때는 리스트로 변환되어 불러와지게 함
//    var message: List<ChatMessageData>
//)
//
