package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.mappers.ItemUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserDataCache
import javax.inject.Inject

class LoadUser @Inject constructor(private val flowersRepository: ItemsRepository,
                                   private val userDataCache: UserDataCache,
                                   private val flowerUiMapper: ItemUiMapper) {
    fun load() {
        val deSerializedUser = userDataCache.deserializeUser()
        val userUi = flowerUiMapper.mapUserToUi(deSerializedUser)
        userUi.items.sortBy { it.item.content }
        flowersRepository.user = userUi
    }
}