package guideme.bydgoszcz.pl.pourtheflower.model

import java.io.Serializable

data class Notification(var enabled: Boolean = false, var repeatDays: Int = 0, var lastNotificationTimeMillis: Long = 0) : Serializable