package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.utils.ByteBufferHelper
import guideme.bydgoszcz.pl.pourtheflower.utils.fromHexToByteArray
import guideme.bydgoszcz.pl.pourtheflower.utils.toHex
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.ByteBuffer
import java.util.Arrays.asList

class FlowerSerializerTest {
    private val serializeResultHex = "0000000100000002494400000007434F4E54454E540000000B4445534352495054494F4E0000000100000008494D41474555524C00"
    private val flowerSerializer = FlowerSerializer()
    private val flower = Flower("ID", "CONTENT", "DESCRIPTION", 1, "IMAGEURL")

    @Test
    fun serialize() {
        val buffer = ByteBuffer.allocate(1024)
        val flowers = asList(flower)

        flowerSerializer.serialize(buffer, flowers)

        val resultHex = ByteBufferHelper.getArray(buffer).toHex()
        assertThat(resultHex).isEqualTo(serializeResultHex)
    }

    @Test
    fun deserializeList() {
        val hexBytes = serializeResultHex.fromHexToByteArray()
        val buffer = ByteBuffer.wrap(hexBytes)

        val list = flowerSerializer.deserializeList(buffer)
        assertThat(list).hasSize(1)
        assertThat(list[0]).isEqualTo(flower)
    }
}