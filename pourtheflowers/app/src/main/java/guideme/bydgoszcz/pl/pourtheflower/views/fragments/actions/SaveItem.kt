package guideme.bydgoszcz.pl.pourtheflower.views.fragments.actions

import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import javax.inject.Inject

class SaveItem @Inject constructor(private val itemsNotifications: ItemsNotifications,
                                   private val saveUserChanges: SaveUserChanges,
                                   private val repository: ItemsRepository) {
    fun saveItem(uiItem: UiItem,
                 onSaved: () -> Unit): Boolean {

        val notification = uiItem.item.notification
        if (notification.enabled) {
            if (notification.lastNotificationTime.isZero()) {
                notification.lastNotificationTime = SystemTime.current().minus(NotificationTime.fromSeconds(1))
            }
        } else {
            notification.lastNotificationTime = SystemTime.ZERO
        }
        repository.user.items.filter {
            it.item.id == uiItem.item.id
        }.forEach {
            repository.user.items.remove(it)
        }
        repository.user.items.add(uiItem)
        saveUserChanges.save {
            itemsNotifications.setUpNotifications(repository.user.items)
            onSaved()
        }
        return true
    }
}