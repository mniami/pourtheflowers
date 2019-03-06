package pl.bydgoszcz.guideme.podlewacz.features

import pl.bydgoszcz.guideme.podlewacz.actions.SaveUserChanges
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import javax.inject.Inject

class AddItemToUser @Inject constructor(private val repo: ItemsRepository,
                                        private val saveChanges: SaveUserChanges) {
    fun add(uiItem: UiItem, onFinished: () -> Unit) {
        if (alreadyExists(uiItem)) {
            onFinished()
            return
        }
        uiItem.isUser = true
        repo.user.items.add(uiItem)
        saveChanges.save(onFinished)
    }

    private fun alreadyExists(uiItem: UiItem): Boolean {
        return repo.user.items.any {
            it.item == uiItem.item
        }
    }
}