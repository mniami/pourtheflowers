package guideme.bydgoszcz.pl.pourtheflower.actions

import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetFlowerPouredNotification @Inject constructor(
        private val itemsNotifications: ItemsNotifications) {
    fun setUp(uiItem: UiItem) {
        uiItem.item.notification.lastNotificationTime = SystemTime.current().minus(NotificationTime.fromSeconds(1)) // second ago
        itemsNotifications.setUpNotification(uiItem)
    }
}