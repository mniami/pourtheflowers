package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.mappers.FlowerUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.FlowersRepository
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserDataCache
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import javax.inject.Inject

class SaveUserChanges @Inject constructor(private val repo: FlowersRepository,
                                          private val userDataCache: UserDataCache,
                                          private val flowerMapper: FlowerUiMapper) {
    fun save(onFinished: () -> Unit) {
        runInBackground {
            val userUi = repo.user
            val user = flowerMapper.mapUserUiToUser(userUi)
            userDataCache.serializeUser(user)
            runOnUi(onFinished)
        }
    }
}