package pl.bydgoszcz.guideme.podlewacz.utils

import pl.bydgoszcz.guideme.podlewacz.utils.TimeHelper.secondsInDay
import pl.bydgoszcz.guideme.podlewacz.utils.TimeHelper.secondsInHour
import pl.bydgoszcz.guideme.podlewacz.utils.TimeHelper.secondsInMinute
import java.io.Serializable

/**
 * Value expressed in seconds
 */
data class NotificationTime(private val _value: Int) : Serializable {
    fun toMillis(): Long {
        return _value.toLong() * TimeHelper.millisInSecond
    }

    val value: Int
        get() = _value

    fun toDays(): Int = Math.round(_value * 1f / secondsInDay)

    operator fun minus(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(_value - notificationTime._value)
    }

    operator fun rem(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(_value % notificationTime._value)
    }

    operator fun plus(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(_value + notificationTime._value)
    }

    operator fun compareTo(notificationTime: NotificationTime): Int {
        return value.compareTo(notificationTime.value)
    }

    override fun toString(): String {
        val days = _value / TimeHelper.secondsInDay
        val hours = (_value / TimeHelper.secondsInHour) % TimeHelper.hoursInDay
        val minutes = (_value / TimeHelper.secondsInMinute) % TimeHelper.minutesInHour
        val seconds = _value % TimeHelper.secondsInMinute

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