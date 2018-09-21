package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.utils.getString
import guideme.bydgoszcz.pl.pourtheflower.utils.putString
import guideme.bydgoszcz.pl.pourtheflower.utils.putStrings
import java.nio.ByteBuffer

class FlowerSerializer {
    companion object {
        private const val MAX_FLOWERS_SIZE = 10000
    }

    fun serialize(byteBuffer: ByteBuffer, flowers: List<Flower>) {
        byteBuffer.putInt(flowers.size)
        flowers.forEach { flower ->
            with(flower) {
                byteBuffer.apply {
                    putStrings(id, content, description)
                    putInt(frequency)
                    putString(imageUrl)
                }
            }
        }
    }

    fun deserializeList(byteBuffer: ByteBuffer): MutableList<Flower> {
        if (!byteBuffer.hasRemaining()) {
            return mutableListOf()
        }
        val size = byteBuffer.int
        val list = ArrayList<Flower>(size)
        val flowerSerializer = FlowerSerializer()

        if (size > MAX_FLOWERS_SIZE) {
            return mutableListOf()
        }
        for (i in 0 until size) {
            list.add(flowerSerializer.deserialize(byteBuffer))
        }
        return list
    }

    fun deserialize(byteBuffer: ByteBuffer): Flower {
        return with(byteBuffer) {
            Flower(getString(),
                    getString(),
                    getString(),
                    int,
                    getString())
        }
    }
}