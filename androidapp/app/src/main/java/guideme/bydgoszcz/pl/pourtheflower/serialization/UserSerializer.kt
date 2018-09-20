package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.User
import java.nio.ByteBuffer

class UserSerializer {
    fun serialize(user: User, byteBuffer: ByteBuffer) {
        FlowerSerializer().serialize(byteBuffer, user.flowers)
    }

    fun deserialize(byteBuffer: ByteBuffer): User {
        if (!byteBuffer.hasRemaining()) {
            return User(mutableListOf())
        }
        return User(FlowerSerializer().deserializeList(byteBuffer))
    }
}
