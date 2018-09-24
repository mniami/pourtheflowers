package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.actions.ReplaceUserFlowers
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.FlowerUiItem
import guideme.bydgoszcz.pl.pourtheflower.model.FlowersRepository
import javax.inject.Inject

class AddFlowerToUser @Inject constructor(private val repo: FlowersRepository,
                                          private val replaceUserFlowers: ReplaceUserFlowers,
                                          private val saveChanges: SaveUserChanges) {
    fun add(flowerUiItem: FlowerUiItem, onFinished: () -> Unit) {
        if (alreadyExists(flowerUiItem)) {
            onFinished()
            return
        }
        flowerUiItem.isUser = true
        repo.user.flowers.add(flowerUiItem)

        replaceUserFlowers.replace()
        saveChanges.save(onFinished)
    }

    private fun alreadyExists(flowerUiItem: FlowerUiItem): Boolean {
        return repo.user.flowers.any {
            it.flower == flowerUiItem.flower
        }
    }
}