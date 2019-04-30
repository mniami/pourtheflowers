package pl.bydgoszcz.guideme.podlewacz.notifications

import android.content.Context
import android.text.Html
import android.text.Spanned
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.model.Notification
import pl.bydgoszcz.guideme.podlewacz.utils.DateTimePart.*
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem

fun Notification.getRemainingNotificationTime(currentTime: SystemTime): NotificationTime {
    return if (lastNotificationTime.millis > 0 && repeatInTime.value > 0) {
        val elapsedTime = currentTime - lastNotificationTime
        var remainTime = repeatInTime - elapsedTime
        val notificationDateTime = currentTime + remainTime

        cleanNotificationTimeToFixedTime(notificationDateTime)

        remainTime = notificationDateTime - currentTime
        remainTime
    } else {
        repeatInTime
    }
}

fun Notification.getRemainingSystemTime(): SystemTime {
    val currentTime = SystemTime.current()
    return currentTime + getRemainingNotificationTime(currentTime)
}

fun Notification.getElapsedTime(): NotificationTime {
    val notificationDateTime = getRemainingSystemTime()
    val currentDateTime = SystemTime.current()

    cleanNotificationTimeToFixedTime(currentDateTime)

    return notificationDateTime - currentDateTime
}

fun UiItem.updateRemainingTime() {
    remainingTime = item.notification.getRemainingNotificationTime(SystemTime.current())
}

fun Notification.getRemainingDaysMessage(context: Context): Spanned {
    val remainingDays = getElapsedTime().toDays()
    if (remainingDays == 0) {
        return Html.fromHtml(context.getString(R.string.flower_frequency_today_label_without_amount_footer))
    }
    var days = if (Math.abs(remainingDays) > 1) R.string.days else R.string.day
    var remainingDaysMessage: String = remainingDays.toString() + " " + context.getString(days)
    return Html.fromHtml(remainingDaysMessage)
//    return Html.fromHtml(when {
//        remainingDays == 0 -> {
//            val messageId = R.string.flower_frequency_today_label
//            context.getString(remainingDays)
//        }
//        remainingDays == -1 -> {
//            val messageId = R.string.flower_frequency_late_yesterday_label
//            context.getString(messageId)
//        }
//        remainingDays < -1 -> {
//            val messageId = R.string.flower_frequency_late_days_label
//            String.format(context.getString(messageId), remainingDays * -1)
//        }
//        remainingDays == 1 -> {
//            context.getString(R.string.flower_frequency_tomorrow_label)
//        }
//        else ->
//            String.format(context.getString(R.string.flower_frequency_in_days_label), remainingDays)
//    })
}

fun cleanNotificationTimeToFixedTime(notificationDateTime: SystemTime) {
    notificationDateTime.set(HOUR_OF_DAY, 8)
    notificationDateTime.set(MINUTE, 0)
    notificationDateTime.set(SECOND, 0)
    notificationDateTime.set(MILLISECOND, 0)
}