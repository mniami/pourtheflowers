package pl.bydgoszcz.guideme.podlewacz.actions

import pl.bydgoszcz.guideme.podlewacz.mappers.ItemUiMapper
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.serialization.UserDataCache
import pl.bydgoszcz.guideme.podlewacz.threads.runInBackground
import pl.bydgoszcz.guideme.podlewacz.threads.runOnUi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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