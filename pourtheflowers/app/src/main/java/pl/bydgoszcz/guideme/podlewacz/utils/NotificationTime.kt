package pl.bydgoszcz.guideme.podlewacz.utils

import pl.bydgoszcz.guideme.podlewacz.utils.TimeHelper.secondsInDay
import java.io.Serializable

/**
 * Value expressed in seconds
 */
data class NotificationTime(private val value: Int) : Serializable {
    fun toMillis(): Long {
        return value.toLong() * TimeHelper.millisInSecond
    }

    val seconds: Int
        get() = value

    fun toDays(): Int = Math.round(value * 1f / secondsInDay)

    operator fun minus(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(value - notificationTime.value)
    }

    operator fun rem(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(value % notificationTime.value)
    }

    operator fun plus(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(value + notificationTime.value)
    }

    operator fun compareTo(notificationTime: NotificationTime): Int {
        return seconds.compareTo(notificationTime.seconds)
    }

    override fun toString(): String {
        val days = value / TimeHelper.secondsInDay
        val hours = (value / TimeHelper.secondsInHour) % TimeHelper.hoursInDay
        val minutes = (value / TimeHelper.secondsInMinute) % TimeHelper.minutesInHour
        val seconds = value % TimeHelper.secondsInMinute

        return "$days $hours:$minutes:$seconds"
    }

    companion object {
        val ZERO: NotificationTime = NotificationTime(0)

        fun fromDays(days: Int) = NotificationTime(days * secondsInDay)
        fun fromHours(hours: Int) = NotificationTime(hours * secondsInHour)
        fun fromMinutes(minutes: Int) = NotificationTime(minutes * secondsInMinute)
        fun fromSeconds(seconds: Int): NotificationTime = NotificationTime(seconds)
        fun fromMillis(millis: Long) = NotificationTime((millis / TimeHelper.millisInSecond).toInt())
    }
}