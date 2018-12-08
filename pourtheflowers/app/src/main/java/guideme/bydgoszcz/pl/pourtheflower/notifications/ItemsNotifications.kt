package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.support.v4.app.FragmentActivity
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem

class ItemsNotifications(private val activity: FragmentActivity, private val saveUserChanges: SaveUserChanges) {
    fun setUpNotifications(items: List<UiItem>) {
        val notificationMonitor = NotificationMonitor(activity)
        items.filter {
            it.item.notification.enabled
        }.forEach {
            val delay = it.item.notification.getRemainingTime(System.currentTimeMillis())
            it.item.notification.lastNotificationTimeMillis = System.currentTimeMillis()
            saveUserChanges.save {
                notificationMonitor.showNotification(
                        it.item.id,
                        it.item.name,
                        "Nadszedł czas podlania",
                        delay)
            }
        }
    }
}
