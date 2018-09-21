package guideme.bydgoszcz.pl.pourtheflower.features

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.model.UserUiItem
import guideme.bydgoszcz.pl.pourtheflower.serialization.DataCache
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserDataCache
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import javax.inject.Inject

class FlowersProvider @Inject constructor(private val context: Context, private val dataCache: DataCache) {
    private lateinit var flowersLibrary: MutableList<FlowerUiItem>
    private lateinit var user: UserUiItem

    private val userSerializer = UserDataCache()
    private val flowerMapper = FlowerUiMapper()
    private val flowersResourcesLoader = FlowersResourcesLoader()

    fun load(onFinished: (FlowersProvider) -> Unit) {
        if (::user.isInitialized) {
            onFinished(this)
            return
        }
        runInBackground {
            loadFlowersFromResources()
            loadUser()
            replaceUserFlowers()
            runOnUi {
                onFinished(this)
            }
        }.onError {
            if (!::user.isInitialized) {
                user = UserUiItem(mutableListOf())
            }
            runOnUi { onFinished(this) }
        }
    }

    fun getUser(): UserUiItem = user
    fun getAllFlowers(): List<FlowerUiItem> = flowersLibrary

    fun addFlowerToUser(flowerUiItem: FlowerUiItem, onFinished: () -> Unit) {
        flowerUiItem.isUser = true
        user.flowers.add(flowerUiItem)
        replaceUserFlowers()

        save(user, onFinished)
    }

    fun removeFlowerFromUser(flowerUiItem: FlowerUiItem, onFinished: () -> Unit) {
        flowersLibrary.filter {
            it.flower.id == flowerUiItem.flower.id
        }.forEach { it.isUser = false }
        user.flowers.remove(flowerUiItem)
        save(user, onFinished)
    }

    private fun save(userUi: UserUiItem, onFinished: () -> Unit) {
        runInBackground {
            val user = flowerMapper.mapUserUiToUser(userUi)
            userSerializer.serializeUser(user, dataCache)
            runOnUi(onFinished)
        }
    }

    private fun loadUser() {
        val deSerializedUser = userSerializer.deserializeUser(dataCache)
        val userUi = flowerMapper.mapUserToUi(deSerializedUser)
        userUi.flowers.sortBy { it.flower.content }
        user = userUi
    }

    private fun replaceUserFlowers() {
        val lib = flowersLibrary
        lib.forEach { allFlower ->
            val flower = user.flowers.firstOrNull { it.flower.id == allFlower.flower.id }
            if (flower != null) {
                flower.isUser = true
                lib[lib.indexOf(allFlower)] = flower
            }
        }
    }

    private fun loadFlowersFromResources() {
        if (!::flowersLibrary.isInitialized) {
            val unSerializedFlowers = flowersResourcesLoader.load(context).sortedBy { it.content }
            flowersLibrary = flowerMapper.mapFlowersToUi(unSerializedFlowers)
        }
    }
}
