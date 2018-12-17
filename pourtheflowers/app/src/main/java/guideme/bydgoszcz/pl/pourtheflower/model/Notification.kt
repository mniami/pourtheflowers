package guideme.bydgoszcz.pl.pourtheflower.model

import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import java.io.Serializable

data class Notification(var enabled: Boolean = false,
                        var repeatInTime: NotificationTime = NotificationTime.ZERO,
                        var lastNotificationTime: SystemTime = SystemTime.ZERO) : Serializable