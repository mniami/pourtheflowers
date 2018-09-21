package guideme.bydgoszcz.pl.pourtheflower.utils

import java.nio.ByteBuffer

object ByteBufferHelper {
    fun getArray(byteBuffer: ByteBuffer): ByteArray {
        val resultByteArray = ByteArray(byteBuffer.position() + 1)
        byteBuffer.rewind()
        byteBuffer.get(resultByteArray)

        return resultByteArray
    }
}