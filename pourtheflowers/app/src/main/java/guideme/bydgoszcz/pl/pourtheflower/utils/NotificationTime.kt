package guideme.bydgoszcz.pl.pourtheflower.utils

import java.io.Serializable

/**
 * Value expressed in seconds
 */
data class NotificationTime(private val value: Int) : Serializable {
    fun getRemaining(notificationTime: NotificationTime): NotificationTime = NotificationTime(value - notificationTime.value % value)

    fun toMillis(): Long {
        return value.toLong() * TimeHelper.millisInSecond
    }

    val seconds: Int
        get() = value

    fun toDays(): Int = value / TimeHelper.secondsInDay

    operator fun minus(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(value - notificationTime.value)
    }

    override fun toString(): String {
        return "NotificationTime(value=$value)"
    }

    companion object {
        val ZERO: NotificationTime = NotificationTime(0)

        fun fromDays(days: Int) = NotificationTime(days * TimeHelper.secondsInDay)
        fun fromMillis(millis: Long) = NotificationTime((millis / TimeHelper.millisInSecond).toInt())
        fun fromSeconds(seconds: Int): NotificationTime = NotificationTime(seconds)
    }
}