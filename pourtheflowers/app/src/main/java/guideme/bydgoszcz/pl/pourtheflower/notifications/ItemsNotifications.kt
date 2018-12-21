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
            val currentTime = SystemTime()
            val delay = it.item.notification.getRemainingTime(currentTime)
            NotificationScheduler.scheduleJob(
                    it.item.id,
                    it.item.name,
                    context.getString(R.string.notification_title),
                    delay.toMillis(),
                    it.item.notification.repeatInTime.toMillis())
        }
    }
}