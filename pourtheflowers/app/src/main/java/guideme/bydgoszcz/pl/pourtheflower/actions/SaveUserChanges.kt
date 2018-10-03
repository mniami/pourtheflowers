package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.mappers.ItemUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserDataCache
import guideme.bydgoszcz.pl.pourtheflower.threads.runInBackground
import guideme.bydgoszcz.pl.pourtheflower.threads.runOnUi
import javax.inject.Inject

class SaveUserChanges @Inject constructor(private val repo: ItemsRepository,
                                          private val userDataCache: UserDataCache,
                                          private val flowerMapper: ItemUiMapper) {
    fun save(onFinished: () -> Unit) {
        runInBackground {
            val userUi = repo.user
            val user = flowerMapper.mapUserUiToUser(userUi)
            userDataCache.save(user)
            runOnUi(onFinished)
        }
    }
}