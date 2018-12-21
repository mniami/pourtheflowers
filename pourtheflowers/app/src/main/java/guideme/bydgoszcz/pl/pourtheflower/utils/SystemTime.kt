package guideme.bydgoszcz.pl.pourtheflower.utils

import java.io.Serializable

/**
 * Value expressed in milliseconds
 */
class SystemTime(timeInMillis: Long = System.currentTimeMillis()) : Serializable {
    private val valueInMillis: Long = timeInMillis

    val millis: Long
        get() {
            return valueInMillis
        }

    operator fun minus(notificationTime: NotificationTime): SystemTime {
        return SystemTime(valueInMillis - notificationTime.toMillis())
    }

    operator fun minus(millis: Int): SystemTime {
        return SystemTime(valueInMillis - millis)
    }

    override fun toString(): String {
        return "SystemTime(valueInMillis=$valueInMillis)"
    }

    fun isZero(): Boolean = this == SystemTime.ZERO

    companion object {
        fun fromDays(days: Int): Int = days * TimeHelper.millisInDay
        val ZERO: SystemTime = SystemTime(0)
    }
}