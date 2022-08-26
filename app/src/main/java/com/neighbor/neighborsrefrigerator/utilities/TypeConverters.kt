package com.neighbor.neighborsrefrigerator.utilities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
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

    fun convertDateToTimeStamp(date: String): Long?{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).parse(date)?.time   //  date를 timestamp로(String -> Long)
        return sdf
    }

    fun convertTimestampToDate(timestamp: Long): String?{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:MM:ss")   //  timestamp를 date로(Long -> String)
        val date = sdf.format(timestamp)
        return date
    }
}
