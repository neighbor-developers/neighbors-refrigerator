package com.neighbor.neighborsrefrigerator.utilities

import com.neighbor.neighborsrefrigerator.data.PostData
import com.neighbor.neighborsrefrigerator.data.UserData
import com.neighbor.neighborsrefrigerator.data.UserSharedPreference

class CalLevel {
    val SHARE_POINT : Int = 5
    val SEEK_POINT : Int = 2

    fun GetUserLevel(postData: List<PostData>): Int {

        var completedPostData = postData.filter {
            it.state == "3"
        }

        val score = completedPostData.fold(0) { total, post ->
            var postScore = if(post.type == 1) SHARE_POINT else SEEK_POINT
            total + postScore
        }

        val levelLine: List<Int> = listOf(0, 5, 15, 30, 60, 100)
        val level = levelLine.count{
            score >= it
        }

        return level
    }
}