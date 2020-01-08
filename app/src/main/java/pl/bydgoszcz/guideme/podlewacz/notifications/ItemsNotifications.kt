package pl.bydgoszcz.guideme.podlewacz.notifications

import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.absoluteValue

@Singleton
class ItemsNotifications @Inject constructor(private val notificationScheduler: NotificationScheduler) {
    fun setUpNotifications(items: List<UiItem>) {
        items.filter {
            it.item.notification.enabled
        }.forEach {
            setUpNotification(it)
        }
    }

    fun setUpNotification(item: UiItem) {
        val currentTime = SystemTime.current()
        var delay = item.item.notification.getRemainingNotificationTime(currentTime)

        if (delay < NotificationTime.ZERO) {
            // pour time has elapsed
            val a = delay.value.absoluteValue
            val b = item.item.notification.repeatInTime.value
            val fx = b - a.rem(b)

            delay = NotificationTime(fx)
        }

        notificationScheduler.scheduleJob(
                delay.toMillis(),
                NotificationTime.fromDays(1).toMillis()) // repeat everyday
    }
}