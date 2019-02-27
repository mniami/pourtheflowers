package guideme.bydgoszcz.pl.pourtheflower.notifications

import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.ContentProvider
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsNotifications @Inject constructor(private val contentProvider: ContentProvider,
                                             private val notificationScheduler: NotificationScheduler) {
    fun setUpNotifications(items: List<UiItem>) {
        items.filter {
            it.item.notification.enabled
        }.forEach {
            setUpNotification(it)
        }
    }

    fun setUpNotification(item: UiItem) {
        val currentTime = SystemTime.current()
        val delay = item.item.notification.getRemainingTime(currentTime)
        val notificationTitle = contentProvider.getString(R.string.notification_title)

        notificationScheduler.scheduleJob(
                item.item.id,
                item.item.name,
                notificationTitle,
                delay.toMillis(),
                item.item.notification.repeatInTime.toMillis())
    }
}