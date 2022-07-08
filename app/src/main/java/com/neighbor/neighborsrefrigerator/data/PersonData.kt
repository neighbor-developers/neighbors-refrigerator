package com.neighbor.neighborsrefrigerator.data

import java.util.*

data class PersonData(
    val response: Response
)
{
    data class Response(
        val header: Header,
        val body: Body
    )
    data class Header(
        val resultCode: Int,
        val resultMsg: String
    )
    data class Body(
        val uId: Int,
        val fbUID: String,
        val email: String,
        val nickname: String,
        val homeAddr: String,
        val reportPoint: Int = 0,
        val createdAt: Date
    )

}


