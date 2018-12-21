package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime

object ItemsNotifications {
    fun setUpNotifications(context : Context, items: List<UiItem>) {
        items.filter {
            it.item.notification.enabled
        }.forEach {
            setUpNotification(context, it)
        }
    }

    fun setUpNotification(context: Context, item: UiItem) {
        val currentTime = SystemTime()
        val delay = item.item.notification.getRemainingTime(currentTime)
        NotificationScheduler.scheduleJob(
                item.item.id,
                item.item.name,
                context.getString(R.string.notification_title),
                delay.toMillis(),
                item.item.notification.repeatInTime.toMillis())
    }
}