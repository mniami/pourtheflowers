package pl.bydgoszcz.guideme.podlewacz.utils

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")
private val dateFormatHuman = SimpleDateFormat("EEEE d MMMM")

/**
 * Value expressed in milliseconds
 */
data class SystemTime(private var timeInMillis: Long) : Serializable {
    val millis: Long
        get() {
            return timeInMillis
        }

    operator fun minus(notificationTime: NotificationTime): SystemTime {
        return SystemTime(timeInMillis - notificationTime.toMillis())
    }

    operator fun minus(millis: Int): SystemTime {
        return SystemTime(timeInMillis - millis)
    }

    operator fun minus(time: SystemTime): NotificationTime {
        return NotificationTime.fromMillis(timeInMillis - time.millis)
    }

    operator fun plus(notificationTime: NotificationTime): SystemTime {
        return SystemTime(timeInMillis + notificationTime.toMillis())
    }

    override fun toString(): String {
        return dateFormat.format(Date(millis))
    }

    fun isZero(): Boolean = this == ZERO

    fun get(part: DateTimePart): Int? {
        val calendar = Calendar.getInstance()
        calendar.time = Date(timeInMillis)
        return calendar.get(mapToCalendarItem(part))
    }

    fun set(part: DateTimePart, value: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = Date(timeInMillis)
        calendar.set(mapToCalendarItem(part), value)
        timeInMillis = calendar.timeInMillis
    }

    fun toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = Date(timeInMillis)
        return calendar
    }

    private fun mapToCalendarItem(part: DateTimePart): Int {
        return when (part) {
            DateTimePart.HOUR_OF_DAY -> Calendar.HOUR_OF_DAY
            DateTimePart.DAY_OF_YEAR -> Calendar.DAY_OF_YEAR
            DateTimePart.MINUTE -> Calendar.MINUTE
            DateTimePart.SECOND -> Calendar.SECOND
            DateTimePart.MILLISECOND -> Calendar.MILLISECOND
        }
    }

    fun isToday(): Boolean {
        val current = SystemTime.current().toCalendar()
        val that = toCalendar()

        return current.get(Calendar.DAY_OF_YEAR) == that.get(Calendar.DAY_OF_YEAR) &&
                current.get(Calendar.YEAR) == that.get(Calendar.YEAR)
    }

    fun getDate(): String {
        return dateFormatHuman.format(Date(millis))
    }

    fun getTime(): String {
        val calendar = toCalendar()
        return "%02d:%02d".format(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE))
    }

    companion object {
        fun current(): SystemTime = SystemTime(timeProvider.current())

        val ZERO: SystemTime = SystemTime(0)
        var timeProvider: TimeProvider = DefaultTimeProvider()
    }
}

class DefaultTimeProvider : TimeProvider

interface TimeProvider {
    fun current(): Long = System.currentTimeMillis()
}
