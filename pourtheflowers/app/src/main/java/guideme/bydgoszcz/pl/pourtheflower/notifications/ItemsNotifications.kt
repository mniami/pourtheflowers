package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.support.v4.app.FragmentActivity
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper.countMillisInDay

class ItemsNotifications(private val activity: FragmentActivity) {
    fun setUpNotifications(items: List<UiItem>) {
        val notificationMonitor = NotificationMonitor(activity)
        items.filter {
            it.item.notification.enabled
        }.forEach {
            val delay = it.item.notification.calculateDelay(System.currentTimeMillis())
            notificationMonitor.showNotification(
                    it.item.id,
                    it.item.content,
                    "Nadszedł czas podlania",
                    delay)
        }
    }
}

fun Notification.calculateDelay(currentTimeMillis: Long): Int {
    return if (lastNotificationTimeMillis > 0) {
        val diffMillis = currentTimeMillis - lastNotificationTimeMillis
        val diffDays = repeatDays - (diffMillis / countMillisInDay) % repeatDays
        (diffDays * countMillisInDay).toInt()
    } else {
        repeatDays * countMillisInDay
    }
}
