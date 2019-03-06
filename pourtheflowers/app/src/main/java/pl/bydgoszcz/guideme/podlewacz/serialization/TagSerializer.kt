package pl.bydgoszcz.guideme.podlewacz.serialization

import pl.bydgoszcz.guideme.podlewacz.model.Tag
import pl.bydgoszcz.guideme.podlewacz.utils.getString
import pl.bydgoszcz.guideme.podlewacz.utils.putString
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