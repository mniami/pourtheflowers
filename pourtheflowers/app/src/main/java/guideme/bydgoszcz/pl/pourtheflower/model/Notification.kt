package guideme.bydgoszcz.pl.pourtheflower.model

import java.io.Serializable

data class Notification(var enabled: Boolean, var repeatDays: Int, var lastNotificationTimeMillis: Long) : Serializable