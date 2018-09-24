package guideme.bydgoszcz.pl.pourtheflower.serialization

import guideme.bydgoszcz.pl.pourtheflower.model.User
import java.nio.ByteBuffer
import javax.inject.Inject

class UserDataCache @Inject constructor(private val dataCache: DataCache) {
    private val userListCacheName = "userList"

    fun deserializeUser(): User {
        val buffer = ByteBuffer.allocate(1024 * 8)
        dataCache.load(userListCacheName, buffer)
        buffer.flip()

        return deserialize(buffer)
    }

    fun serializeUser(user: User) {
        val buffer = ByteBuffer.allocate(1024 * 8)
        serialize(user, buffer)
        dataCache.save(userListCacheName, buffer)
    }

    private fun serialize(user: User, byteBuffer: ByteBuffer) {
        FlowerSerializer().serialize(byteBuffer, user.flowers)
    }

    private fun deserialize(byteBuffer: ByteBuffer): User {
        if (!byteBuffer.hasRemaining()) {
            return User(mutableListOf())
        }
        return User(FlowerSerializer().deserializeList(byteBuffer))
    }
}
