package guideme.bydgoszcz.pl.pourtheflower.utils

import java.io.Serializable

/**
 * Value expressed in milliseconds
 */
data class SystemTime(private val timeInMillis: Long) : Serializable {
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

    override fun toString(): String {
        return "SystemTime(valueInMillis=$timeInMillis)"
    }

    fun isZero(): Boolean = this == SystemTime.ZERO

    companion object {
        fun fromDays(days: Int): Int = days * TimeHelper.millisInDay
        fun current() : SystemTime = SystemTime(System.currentTimeMillis())
        val ZERO: SystemTime = SystemTime(0)

    }
}