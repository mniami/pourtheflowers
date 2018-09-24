package guideme.bydgoszcz.pl.pourtheflower.features

import guideme.bydgoszcz.pl.pourtheflower.actions.ReplaceUserItems
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import javax.inject.Inject

class AddItemToUser @Inject constructor(private val repo: ItemsRepository,
                                        private val replaceUserItems: ReplaceUserItems,
                                        private val saveChanges: SaveUserChanges) {
    fun add(uiItem: UiItem, onFinished: () -> Unit) {
        if (alreadyExists(uiItem)) {
            onFinished()
            return
        }
        uiItem.isUser = true
        repo.user.items.add(uiItem)

        replaceUserItems.replace()
        saveChanges.save(onFinished)
    }

    private fun alreadyExists(uiItem: UiItem): Boolean {
        return repo.user.items.any {
            it.item == uiItem.item
        }
    }
}