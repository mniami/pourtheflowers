package guideme.bydgoszcz.pl.pourtheflower.features

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.NotificationPresenter
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