package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.mappers.FlowerUiMapper
import guideme.bydgoszcz.pl.pourtheflower.model.FlowersRepository
import guideme.bydgoszcz.pl.pourtheflower.serialization.UserDataCache
import javax.inject.Inject

class LoadUser @Inject constructor(private val flowersRepository: FlowersRepository,
                                   private val userDataCache: UserDataCache,
                                   private val flowerUiMapper: FlowerUiMapper) {
    fun load() {
        val deSerializedUser = userDataCache.deserializeUser()
        val userUi = flowerUiMapper.mapUserToUi(deSerializedUser)
        userUi.flowers.sortBy { it.flower.content }
        flowersRepository.user = userUi
    }
}