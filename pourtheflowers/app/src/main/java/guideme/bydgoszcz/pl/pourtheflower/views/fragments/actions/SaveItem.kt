package guideme.bydgoszcz.pl.pourtheflower.views.fragments.actions

import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.actions.SetFlowerPouredNotification
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import javax.inject.Inject

class SaveItem @Inject constructor(private val itemsNotifications: ItemsNotifications,
                                   private val setFlowerPouredNotification: SetFlowerPouredNotification,
                                   private val saveUserChanges: SaveUserChanges,
                                   private val repository: ItemsRepository) {
    fun saveItem(uiItem: UiItem,
                 etName: String,
                 etDescription: String,
                 pourFrequencyInDays: Int,
                 onSaved: () -> Unit): Boolean {

        with(uiItem.item) {
            if (pourFrequencyInDays > 0) {
                notification.repeatInTime = NotificationTime.fromDays(pourFrequencyInDays)
                notification.enabled = true
            } else {
                notification.enabled = false
            }
            kotlin.with(uiItem.item) {
                if (notification.enabled) {
                    if (notification.lastNotificationTime.isZero()) {
                        setFlowerPouredNotification.setUp(uiItem)
                    }
                } else {
                    notification.lastNotificationTime = SystemTime.ZERO
                }
            }
            name = etName
            description = etDescription

            repository.user.items.filter {
                it.item.id == id
            }.forEach {
                repository.user.items.remove(it)
            }
            repository.user.items.add(uiItem)
        }
        itemsNotifications.setUpNotifications(repository.user.items)
        saveUserChanges.save {
            onSaved()
        }
        return true
    }
}