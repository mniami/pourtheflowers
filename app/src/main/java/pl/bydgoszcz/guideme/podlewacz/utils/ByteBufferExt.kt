package pl.bydgoszcz.guideme.podlewacz.utils

import java.nio.ByteBuffer

fun ByteBuffer.putString(text: String) {
    val bytes = text.toByteArray(Charsets.UTF_8)
    putInt(bytes.size)
    put(bytes)
}

fun ByteBuffer.putStrings(vararg texts: String) {
    texts.forEach {
        putString(it)
    }
}

fun ByteBuffer.getString(): String = ByteBufferHelper.getString(this)
