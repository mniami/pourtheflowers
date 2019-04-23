package pl.bydgoszcz.guideme.podlewacz.notifications

import androidx.work.WorkManager
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProvider
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
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
            val a = delay.value.absoluteValue
            val b = item.item.notification.repeatInTime.value
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