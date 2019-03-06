package pl.bydgoszcz.guideme.podlewacz.views.fragments.actions

import pl.bydgoszcz.guideme.podlewacz.actions.SaveUserChanges
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.ItemsNotifications
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
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