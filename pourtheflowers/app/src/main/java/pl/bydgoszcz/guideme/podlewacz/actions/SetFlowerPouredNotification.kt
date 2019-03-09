package pl.bydgoszcz.guideme.podlewacz.actions

import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.ItemsNotifications
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
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