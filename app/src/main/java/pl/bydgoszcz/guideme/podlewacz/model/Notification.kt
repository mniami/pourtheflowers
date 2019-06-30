package pl.bydgoszcz.guideme.podlewacz.model

import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import java.io.Serializable

data class Notification(var enabled: Boolean = false,
                        var repeatInTime: NotificationTime = NotificationTime.ZERO,
                        var lastNotificationTime: SystemTime = SystemTime.ZERO) : Serializable