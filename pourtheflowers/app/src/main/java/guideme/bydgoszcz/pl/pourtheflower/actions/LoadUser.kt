package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.mappers.ItemUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserDataCache
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