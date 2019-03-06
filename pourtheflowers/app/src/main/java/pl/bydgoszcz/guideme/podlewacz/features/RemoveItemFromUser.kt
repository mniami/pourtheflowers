package pl.bydgoszcz.guideme.podlewacz.features

import android.content.Context
import pl.bydgoszcz.guideme.podlewacz.actions.SaveUserChanges
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.NotificationPresenter
import javax.inject.Inject

class RemoveItemFromUser @Inject constructor(private val repo: ItemsRepository,
                                             private val saveUserChanges: SaveUserChanges,
                                             private val context: Context) {
    fun remove(flowerUiItem: UiItem, onFinished: () -> Unit) {
        val lib = repo.itemsStore
        val user = repo.user
        lib.filter {
            it.item.id == flowerUiItem.item.id
        }.forEach { it.isUser = false }
        user.items.remove(flowerUiItem)
        NotificationPresenter.removeNotification(context, flowerUiItem.item.id)
        saveUserChanges.save(onFinished)
    }
}