package pl.bydgoszcz.guideme.podlewacz.actions

import pl.bydgoszcz.guideme.podlewacz.mappers.ItemUiMapper
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.serialization.UserDataCache
import javax.inject.Inject

class LoadUser @Inject constructor(private val repository: ItemsRepository,
                                   private val userDataCache: UserDataCache,
                                   private val itemUiMapper: ItemUiMapper) {
    fun load() {
        val deSerializedUser = userDataCache.load()
        val userUi = itemUiMapper.mapUserToUi(deSerializedUser, true)
        userUi.items.sortBy { it.item.name }
        repository.user = userUi
    }
}