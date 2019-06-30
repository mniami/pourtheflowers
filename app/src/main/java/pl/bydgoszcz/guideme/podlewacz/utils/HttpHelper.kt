package pl.bydgoszcz.guideme.podlewacz.utils

import com.google.gson.GsonBuilder
import java.net.URL

val gson = GsonBuilder().create()

inline fun <reified T> URL.readJson(): T {
    return gson.fromJson<T>(readText(), T::class.java)
}