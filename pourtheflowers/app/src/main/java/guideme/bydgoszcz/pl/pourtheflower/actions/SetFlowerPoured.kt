package guideme.bydgoszcz.pl.pourtheflower.actions

import android.content.Context
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime

object SetFlowerPoured {
    fun set(context: Context, uiItem: UiItem) {
        uiItem.item.notification.lastNotificationTime = SystemTime().minus(NotificationTime.fromSeconds(1)) // second ago
        ItemsNotifications.setUpNotification(context, uiItem)
    }
}