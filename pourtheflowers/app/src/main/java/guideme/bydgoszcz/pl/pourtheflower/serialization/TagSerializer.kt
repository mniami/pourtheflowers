package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.Tag
import guideme.bydgoszcz.pl.pourtheflower.utils.getString
import guideme.bydgoszcz.pl.pourtheflower.utils.putString
import java.nio.ByteBuffer

class TagSerializer {
    fun serialize(byteBuffer: ByteBuffer, tags: List<Tag>) {
        byteBuffer.putInt(tags.size)
        tags.forEach {
            byteBuffer.putString(it.category)
            byteBuffer.putString(it.value)
        }
    }

    fun deserialize(byteBuffer: ByteBuffer): List<Tag> {
        val list = mutableListOf<Tag>()
        with(byteBuffer) {
            val size = int

            for (i in 0 until size) {
                val category = getString()
                val value = getString()

                list.add(Tag(category, value))
            }
        }
        return list
    }
}