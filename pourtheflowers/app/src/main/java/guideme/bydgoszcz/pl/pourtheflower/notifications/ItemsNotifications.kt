package guideme.bydgoszcz.pl.pourtheflower.notifications

import androidx.work.WorkManager
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.ContentProvider
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.absoluteValue

@Singleton
class ItemsNotifications @Inject constructor(private val contentProvider: ContentProvider,
                                             private val notificationScheduler: NotificationScheduler) {
    fun setUpNotifications(items: List<UiItem>) {
        WorkManager.getInstance().cancelAllWork()
        items.filter {
            it.item.notification.enabled
        }.forEach {
            setUpNotification(it)
        }
    }

    fun setUpNotification(item: UiItem) {
        val currentTime = SystemTime.current()
        var delay = item.item.notification.getRemainingTime(currentTime)
        val notificationTitle = contentProvider.getString(R.string.notification_title)

        if (delay < NotificationTime.ZERO) {
            // pour time has elapsed
            val a = delay.seconds.absoluteValue
            val b = item.item.notification.repeatInTime.seconds
            val fx = b - a.rem(b)

            delay = NotificationTime(fx)
        }

        notificationScheduler.scheduleJob(
                item.item.id,
                item.item.name,
                notificationTitle,
                delay.toMillis(),
                item.item.notification.repeatInTime.toMillis())
    }
}