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
    private lateinit var flowersLibrary: List<FlowerUiItem>
    private lateinit var user: UserUiItem

    private val userSerializer = UserDataCache()
    private val flowerMapper = FlowerUiMapper()
    private val flowersResourcesLoader = FlowersResourcesLoader()

    fun load(onFinished: (FlowersProvider) -> Unit) {
        runInBackground {
            loadFlowersFromResources()
            loadUser()
            markUsersFlowers()
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

    fun save(userUi: UserUiItem, onFinished: () -> Unit) {
        runInBackground {
            val user = flowerMapper.mapUserUiToUser(userUi)
            userSerializer.serializeUser(user, dataCache)
            runOnUi(onFinished)
        }
    }

    private fun loadUser() {
        val deSerializedUser = userSerializer.deserializeUser(dataCache)
        user = flowerMapper.mapUserToUi(deSerializedUser)
    }

    private fun markUsersFlowers() {
        flowersLibrary.filter { allFlower ->
            user.flowers.any { it.flower.id == allFlower.flower.id }
        }.forEach {
            it.isUser = true
        }
    }

    private fun loadFlowersFromResources() {
        if (!::flowersLibrary.isInitialized) {
            val unSerializedFlowers = flowersResourcesLoader.load(context)
            flowersLibrary = flowerMapper.mapFlowersToUi(unSerializedFlowers)
        }
    }
}
