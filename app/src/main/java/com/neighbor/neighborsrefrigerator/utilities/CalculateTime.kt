package com.neighbor.neighborsrefrigerator.utilities

import java.text.SimpleDateFormat
import java.util.*

class CalculateTime {
    // 60000 = 1분
    // 3600000 = 1시간
    // 86400000 = 1일

    fun calTimeToChat(current: Long, date: String): String {
        val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).parse(date)?.time


        return if (604800000 > (current - formattedTime!!) && (current - formattedTime) >= 86400000)
            "${(current - formattedTime) / 86400000}일전"
        // 1일 미만
        else if (current - formattedTime < 86400000)
            SimpleDateFormat("hh:MM", Locale.KOREA).format(formattedTime)
        // 1분 미만
        else if (current - formattedTime <= 60000)
            "방금"
        // 일주일 이상
        else
            SimpleDateFormat("mm.dd", Locale.KOREA).format(formattedTime)
    }

    fun calTimeToPost(current: Long, date: String): String{
        val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.KOREA).parse(date)?.time

        // 1분 미만
        return if (current - formattedTime!! < 60000)
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

    }
}