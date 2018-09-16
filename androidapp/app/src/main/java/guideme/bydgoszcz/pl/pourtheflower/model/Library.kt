package guideme.bydgoszcz.pl.pourtheflower.model

import java.nio.ByteBuffer

class Library(val flowers: List<Flower>) {
    fun serialize(byteBuffer: ByteBuffer) {
        byteBuffer.apply {
            putInt(flowers.size)
            flowers.forEach {

            }
        }
    }
}