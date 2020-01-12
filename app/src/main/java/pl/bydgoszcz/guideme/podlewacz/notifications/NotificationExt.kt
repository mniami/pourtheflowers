package pl.bydgoszcz.guideme.podlewacz.notifications

import android.content.Context
import android.icu.util.Calendar
import android.text.Html
import android.text.Spanned
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.model.Notification
import pl.bydgoszcz.guideme.podlewacz.utils.DateTimePart.*
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime.Companion.now
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import kotlin.math.abs

fun Notification.getRemainingNotificationTime(currentTime: SystemTime): NotificationTime {
    return getRemainingSystemTime(currentTime) - currentTime
}

fun Notification.getRemainingSystemTime(currentTime: SystemTime = now()): SystemTime {
    return if (lastNotificationTime.millis > 0 && repeatInTime.value > 0) {
        val elapsedTime = currentTime - lastNotificationTime
        var remainTime = repeatInTime - elapsedTime
        val notificationDateTime = currentTime + remainTime

        cleanNotificationTimeToFixedTime(notificationDateTime)
        notificationDateTime
    } else {
        throw IllegalArgumentException("Last notification time or repeat in time values cannot be 0")
    }
}

fun Notification.getElapsedTime(): NotificationTime {
    val notificationDateTime = getRemainingSystemTime()
    val currentDateTime = now()

    cleanNotificationTimeToFixedTime(currentDateTime)

    return notificationDateTime - currentDateTime
}

fun UiItem.updateRemainingTime() {
    remainingTime = item.notification.getRemainingNotificationTime(now())
}

fun Notification.getRemainingDaysMessage(context: Context): Spanned {
    val remainingDays = (getRemainingSystemTime() - cleanNotificationTimeToFixedTime(now())).toDays()
    if (remainingDays == 0) {
        return Html.fromHtml(context.getString(R.string.flower_frequency_today_label_without_amount_footer))
    }
    var days = if (abs(remainingDays) > 1) R.string.days else R.string.day
    var remainingDaysMessage: String = remainingDays.toString() + " " + context.getString(days)
    return Html.fromHtml(remainingDaysMessage)
}

fun cleanNotificationTimeToFixedTime(notificationDateTime: SystemTime) : SystemTime {
    notificationDateTime.set(HOUR_OF_DAY, 8)
    notificationDateTime.set(MINUTE, 0)
    notificationDateTime.set(SECOND, 0)
    notificationDateTime.set(MILLISECOND, 0)
    return notificationDateTime
}