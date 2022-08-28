package com.neighbor.neighborsrefrigerator.utilities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.neighbor.neighborsrefrigerator.data.ChatMessage
import java.io.ByteArrayOutputStream
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MyTypeConverters {
    // List에서 String으로 변환
    @TypeConverter
    fun convertListToJson(value: List<ChatMessage>?): String = Gson().toJson(value)

    // string에서 List로 변환
    @TypeConverter
    fun convertJsonToList(value: String) = Gson().fromJson(value, Array<ChatMessage>::class.java).toList()

    // Bitmap -> ByteArray 변환
    @TypeConverter
    fun toByteArray(bitmap : Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun toBitmap(bytes : ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    @TypeConverter
    fun convertDateToTimeStamp(date: String): Long?{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).parse(date)?.time   //  date를 timestamp로(String -> Long)
        return sdf
    }
    @TypeConverter
    fun convertTimestampToDate(timestamp: Long): String?{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:MM:ss")   //  timestamp를 date로(Long -> String)
        val date = sdf.format(timestamp)
        return date
    }
    // 일단 써보고 맞게 바꾸기
    fun convertTimestampToStringDate(current: Long, timestamp: Long): String? {

        return if (604800000 > (current - timestamp!!) && (current - timestamp) >= 86400000)
            "${(current - timestamp) / 86400000}일전"
        // 1일 미만
        else if (current - timestamp < 86400000)
            SimpleDateFormat("hh:MM", Locale.KOREA).format(timestamp)
        // 1분 미만
        else if (current - timestamp <= 60000)
            "방금"
        // 일주일 이상
        else
            SimpleDateFormat("mm.dd", Locale.KOREA).format(timestamp)
    }
}
