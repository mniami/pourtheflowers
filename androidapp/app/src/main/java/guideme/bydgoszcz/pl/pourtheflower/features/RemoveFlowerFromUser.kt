package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.model.FlowersRepository
import javax.inject.Inject

class RemoveFlowerFromUser @Inject constructor(private val repo: FlowersRepository,
                                               private val saveUserChanges: SaveUserChanges) {
    fun remove(flowerUiItem: FlowerUiItem, onFinished: () -> Unit) {
        val lib = repo.lib
        val user = repo.user
        lib.filter {
            it.flower.id == flowerUiItem.flower.id
        }.forEach { it.isUser = false }
        user.flowers.remove(flowerUiItem)
        saveUserChanges.save(onFinished)
    }
}