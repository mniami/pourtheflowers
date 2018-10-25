package guideme.bydgoszcz.pl.pourtheflower.notifications

import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper

fun Notification.calculateDelay(currentTimeMillis: Long): Int {
    return if (lastNotificationTimeMillis > 0) {
        val diffMillis = currentTimeMillis - lastNotificationTimeMillis
        val diffDays = repeatDays - (diffMillis / TimeHelper.countMillisInDay) % repeatDays
        (diffDays * TimeHelper.countMillisInDay).toInt()
    } else {
        repeatDays * TimeHelper.countMillisInDay
    }
}