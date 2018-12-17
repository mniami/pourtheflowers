package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.content.Context
import android.graphics.Color
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource

fun Notification.getRemainingTime(currentNotificationTime: SystemTime): NotificationTime {
    return if (lastNotificationTime.value > 0 && repeatInTime.value > 0) {
        val diffMillis = NotificationTime.fromMillis(currentNotificationTime.value - lastNotificationTime.value)
        val diffTime = repeatInTime.getRemaining(diffMillis)
        diffTime
    } else {
        repeatInTime
    }
}

class ColorHelper {
    companion object {
        fun getColor(colorResource : Int, context: Context) : Int {
            return context.resources.getColorFromResource(colorResource)
        }
    }
}
fun Notification.getBackgroundColor(context: Context, remainingDays: Int) : Int {
    return when(remainingDays) {
        1 -> Color.RED
        in 2..3 -> ColorHelper.getColor(R.color.orange, context)
        else -> ColorHelper.getColor(R.color.gray, context)
    }
}