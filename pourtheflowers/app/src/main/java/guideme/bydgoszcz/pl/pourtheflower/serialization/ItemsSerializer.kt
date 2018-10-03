package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.utils.getString
import guideme.bydgoszcz.pl.pourtheflower.utils.putStrings
import java.nio.ByteBuffer

class ItemsSerializer {
    companion object {
        private const val MAX_ITEMS_SIZE = 10000
    }

    fun serialize(byteBuffer: ByteBuffer, items: List<Item>) {
        byteBuffer.putInt(items.size)
        items.forEach { item ->
            with(item) {
                byteBuffer.apply {
                    putStrings(id, content, description, imageUrl)
                    TagSerializer().serialize(byteBuffer, tags)
                }
            }
        }
    }

    fun deserializeList(byteBuffer: ByteBuffer): MutableList<Item> {
        if (!byteBuffer.hasRemaining()) {
            return mutableListOf()
        }
        val size = byteBuffer.int
        val list = ArrayList<Item>(size)

        if (size > MAX_ITEMS_SIZE) {
            return mutableListOf()
        }
        for (i in 0 until size) {
            list.add(deserialize(byteBuffer))
        }
        return list
    }

    private fun deserialize(byteBuffer: ByteBuffer): Item {
        val tagSerializer = TagSerializer()
        return with(byteBuffer) {
            Item(getString(),
                    getString(),
                    getString(),
                    getString(),
                    tagSerializer.deserialize(byteBuffer))
        }
    }
}