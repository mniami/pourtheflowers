package guideme.bydgoszcz.pl.pourtheflower.notifications

import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime

fun Notification.getRemainingTime(currentNotificationTime: SystemTime): NotificationTime {
    return if (lastNotificationTime.millis > 0 && repeatInTime.seconds > 0) {
        val diffMillis = NotificationTime.fromMillis(currentNotificationTime.millis - lastNotificationTime.millis)
        val diffTime = repeatInTime.getRemaining(diffMillis)
        diffTime
    } else {
        repeatInTime
    }
}

fun UiItem.updateRemainingTime() {
    remainingTime = item.notification.getRemainingTime(SystemTime.current())
}