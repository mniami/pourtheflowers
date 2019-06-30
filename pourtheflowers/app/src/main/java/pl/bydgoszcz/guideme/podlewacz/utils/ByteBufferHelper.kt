package pl.bydgoszcz.guideme.podlewacz.utils

import java.nio.ByteBuffer

object ByteBufferHelper {
    val MAX_STRING_LENGTH = 1024

    fun getArray(byteBuffer: ByteBuffer): ByteArray {
        val resultByteArray = ByteArray(byteBuffer.position() + 1)
        byteBuffer.rewind()
        byteBuffer.get(resultByteArray)

        return resultByteArray
    }

    fun getString(byteBuffer: ByteBuffer): String {
        val size = byteBuffer.int
        if (size > MAX_STRING_LENGTH) {
            throw IllegalArgumentException()
        }
        val byteArray = ByteArray(size)
        byteBuffer.get(byteArray)
        return byteArray.toString(Charsets.UTF_8)
    }
}