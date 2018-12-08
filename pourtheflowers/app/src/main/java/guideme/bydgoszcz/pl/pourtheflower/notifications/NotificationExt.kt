package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.content.Context
import android.graphics.Color
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource

fun Notification.getRemainingTime(currentTimeMillis: Long): Int {
    return getRemainingDays(currentTimeMillis) * TimeHelper.countMillisInDay
}

fun Notification.getRemainingDays(currentTimeMillis: Long) : Int {
    return if (lastNotificationTimeMillis > 0) {
        val diffMillis = currentTimeMillis - lastNotificationTimeMillis
        val diffDays = repeatDays - (diffMillis / TimeHelper.countMillisInDay) % repeatDays
        diffDays.toInt()
    } else {
        repeatDays
    }
}
class ColorHelper {
    companion object {
        fun getColor(colorResource : Int, context: Context) : Int {
            return context.resources.getColorFromResource(colorResource)
        }
    }
}
fun Notification.getBackgroundColor(context: Context) : Int {
    val remainingDays = getRemainingDays(System.currentTimeMillis())
    return when(remainingDays) {
        1 -> Color.RED
        in 2..3 -> ColorHelper.getColor(R.color.orange, context)
        else -> ColorHelper.getColor(R.color.gray, context)
    }
}