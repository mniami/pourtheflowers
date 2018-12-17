package guideme.bydgoszcz.pl.pourtheflower.notifications

import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime

class ItemsNotifications(private val saveUserChanges: SaveUserChanges) {
    fun setUpNotifications(items: List<UiItem>) {
        items.filter {
            it.item.notification.enabled
        }.forEach {
            val currentTime = SystemTime()
            val delay = it.item.notification.getRemainingTime(currentTime)
            if (it.item.notification.lastNotificationTime.value == 0L) {
                it.item.notification.lastNotificationTime = SystemTime() - SystemTime.seconds(1)
            }
            saveUserChanges.save {
                NotificationJob.scheduleJob(
                        it.item.id,
                        it.item.name,
                        "Nadszedł czas podlania",
                        delay.toMillis(),
                        it.item.notification.repeatInTime.toMillis())
            }
        }
    }
}
