package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.support.v4.app.FragmentActivity
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem

class ItemsNotifications(private val activity: FragmentActivity) {
    fun setUpNotifications(items: List<UiItem>) {
        val notificationMonitor = NotificationMonitor(activity)
        items.filter {
            it.item.notification.enabled
        }.forEach {
            val delay = it.item.notification.calculateDelay(System.currentTimeMillis())
            notificationMonitor.showNotification(
                    it.item.id,
                    it.item.name,
                    "Nadszedł czas podlania",
                    delay)
        }
    }
}
