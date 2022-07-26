package com.neighbor.neighborsrefrigerator.data

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.Serializable

inline fun <reified T : Serializable> serializableNavType(
    isNullableAllowed: Boolean = false
): NavType<T> {
    return object : NavType<T>(isNullableAllowed) {
        override val name: String
            get() = "SupportSerializable"

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putSerializable(key, value) // Bundle에 Serializable 타입으로 추가
        }

        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getSerializable(key) as? T // Bundle에서 Serializable 타입으로 꺼낸다
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(value) // String 전달된 Parsing 방법을 정의
        }
    }
}

