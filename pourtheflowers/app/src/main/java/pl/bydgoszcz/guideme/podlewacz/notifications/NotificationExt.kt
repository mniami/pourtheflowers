package pl.bydgoszcz.guideme.podlewacz.notifications

import pl.bydgoszcz.guideme.podlewacz.model.Notification
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime

fun Notification.getRemainingTime(currentTime: SystemTime): NotificationTime {
    return if (lastNotificationTime.millis > 0 && repeatInTime.seconds > 0) {
        val elapsedTime = currentTime - lastNotificationTime
        val diffTime = repeatInTime - elapsedTime
        diffTime
    } else {
        repeatInTime
    }
}

fun UiItem.updateRemainingTime() {
    remainingTime = item.notification.getRemainingTime(SystemTime.current())
}