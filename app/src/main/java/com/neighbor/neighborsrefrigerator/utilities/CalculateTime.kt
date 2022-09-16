package com.neighbor.neighborsrefrigerator.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class CalculateTime {
    // 60000 = 1분
    // 3600000 = 1시간
    // 86400000 = 1일

    fun calTimeToChat(current: Long, chatTime: Long): String {

        return if (604800000 > (current - chatTime) && (current - chatTime) >= 86400000)
            "${(current - chatTime) / 86400000}일전"
        // 1일 미만
        else if (current - chatTime < 86400000)
            SimpleDateFormat("hh:MM", Locale.KOREA).format(chatTime)
        // 1분 미만
        else if (current - chatTime <= 60000)
            "방금"
        // 일주일 이상
        else
            SimpleDateFormat("mm.dd", Locale.KOREA).format(chatTime)
    }

    fun calTimeToPost(_current: Long, _date: String): String{
        val date = _date.split(".")[0].replace("T", " ")
        val formattedTime = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA).parse(date)?.time
        val cuDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA).format(_current)

        val current = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA).parse(cuDate)?.time

        Log.d("time", "${current!! - formattedTime!!}")

        // 1분 미만
        return if (current != null && formattedTime != null) {
            if (current - formattedTime < 60000)
                "방금"
            // 1시간 미만
            else if (current - formattedTime in 60000..3599999)
                "${(current - formattedTime) / 60000}분 전"
            // 1시간 초과 24시간 미만
            else if (current - formattedTime in 3600000..86399999)
                "${(current - formattedTime) / 86400000}시간 전"
            // 24시간 이상
            else
                "${(current - formattedTime) / 108000000}일 전"
        }else
            ""


    }
}