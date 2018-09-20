package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.utils.getString
import guideme.bydgoszcz.pl.pourtheflower.utils.putString
import guideme.bydgoszcz.pl.pourtheflower.utils.putStrings
import java.nio.ByteBuffer

class FlowerSerializer {
    fun serialize(byteBuffer: ByteBuffer, flowers: Iterable<Flower>) {
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
        for (i in 0..size) {
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