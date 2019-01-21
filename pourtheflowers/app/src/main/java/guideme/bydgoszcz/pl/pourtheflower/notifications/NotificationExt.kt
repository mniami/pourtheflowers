package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.content.Context
import android.graphics.Color
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.ColorHelper
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

fun UiItem.getPassedTime(): NotificationTime {
    return item.notification.repeatInTime - remainingTime
}

fun UiItem.updateRemainingTime() {
    remainingTime = item.notification.getRemainingTime(SystemTime.current())
}
fun Notification.getBackgroundColor(remainingDays: Int) : Int {
    return when {
        remainingDays >= 0 -> R.color.colorPrimary
        else -> android.R.color.holo_red_dark
    }
}