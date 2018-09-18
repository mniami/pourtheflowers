package guideme.bydgoszcz.pl.pourtheflower

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import guideme.bydgoszcz.pl.pourtheflower.model.User
import guideme.bydgoszcz.pl.pourtheflower.serialization.DataCache
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserSerializer
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import java.nio.ByteBuffer
import javax.inject.Inject

class FlowersProvider @Inject constructor(val context: Context) {
    private lateinit var flowers: List<Flower>
    private lateinit var user: User

    private val userListCacheName = "userList"
    private val dataCache: DataCache = DataCache(context.cacheDir.absolutePath)
    private val flowersListLoader = FlowersListLoader()

    fun load(onFinished: (FlowersProvider) -> Unit) {
        runInBackground {
            loadUserList()
            loadFlowers()
            runOnUi {
                onFinished(this)
            }
        }
    }

    fun getUser(): User = user
    fun getAllFlowers(): List<Flower> = flowers

    private fun loadUserList() {
        val buffer = ByteBuffer.allocate(1024 * 8)
        dataCache.load(userListCacheName, buffer)
        user = UserSerializer().deserialize(buffer)
    }

    private fun loadFlowers() {
        if (!::flowers.isInitialized) {
            flowers = flowersListLoader.load(context)
        }
    }
}
