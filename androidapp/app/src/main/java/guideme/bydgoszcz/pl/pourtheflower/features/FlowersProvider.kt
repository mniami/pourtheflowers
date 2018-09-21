package guideme.bydgoszcz.pl.pourtheflower.features

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.model.User
import guideme.bydgoszcz.pl.pourtheflower.serialization.DataCache
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserSerializer
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import java.nio.ByteBuffer
import javax.inject.Inject

class FlowersProvider @Inject constructor(private val context: Context, private val dataCache: DataCache) {
    private lateinit var flowers: List<Flower>
    private lateinit var user: User

    private val userListCacheName = "userList"
    private val flowersListLoader = FlowersListLoader()

    fun load(onFinished: (FlowersProvider) -> Unit) {
        runInBackground {
            loadFlowers()
            loadUserList()
            runOnUi {
                onFinished(this)
            }
        }.onError {
            runOnUi { onFinished(this) }
        }
    }

    fun getUser(): User = user
    fun getAllFlowers(): List<Flower> = flowers

    private fun loadUserList() {
        val buffer = ByteBuffer.allocate(1024 * 8)
        dataCache.load(userListCacheName, buffer)
        buffer.flip()
        user = UserSerializer().deserialize(buffer)
    }

    private fun loadFlowers() {
        if (!::flowers.isInitialized) {
            flowers = flowersListLoader.load(context)
        }
    }

    fun save(user: User, onFinished: () -> Unit) {
        runInBackground {
            val buffer = ByteBuffer.allocate(1024 * 8)
            UserSerializer().serialize(user, buffer)
            dataCache.save(userListCacheName, buffer)

            runOnUi(onFinished)
        }
    }
}
