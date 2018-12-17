package guideme.bydgoszcz.pl.pourtheflower.utils

object TimeHelper {
    const val timeToMillis = 1000
    const val timeUnit = 86400000 / timeToMillis / 3600
}

class SystemTime(timeInMillis: Long = System.currentTimeMillis()) {
    private val valueInMillis: Long = timeInMillis

    val value: Long
        get() {
            return valueInMillis
        }

    operator fun minus(notificationTime: NotificationTime): SystemTime {
        return SystemTime(valueInMillis - notificationTime.toMillis())
    }
    operator fun minus(millis : Int): SystemTime {
        return SystemTime(valueInMillis - millis)
    }

    override fun toString(): String {
        return "SystemTime(valueInMillis=$valueInMillis)"
    }

    companion object {
        fun seconds(seconds: Int): Int = seconds * TimeHelper.timeToMillis

        val ZERO: SystemTime = SystemTime(0)
    }
}

data class NotificationTime(val value: Int) {
    fun getRemaining(diffMillis: NotificationTime): NotificationTime = NotificationTime(value - diffMillis.value % value)

    fun toMillis(): Long {
        return value.toLong() * TimeHelper.timeToMillis
    }

    fun toDays(): Int = value / TimeHelper.timeUnit

    operator fun minus(notificationTime: NotificationTime): NotificationTime {
        return NotificationTime(value - notificationTime.value)
    }

    override fun toString(): String {
        return "NotificationTime(value=$value)"
    }

    companion object {
        val ZERO: NotificationTime = NotificationTime(0)

        fun fromDays(days: Int) = NotificationTime(days * TimeHelper.timeUnit)
        fun fromMillis(millis: Long) = NotificationTime((millis / TimeHelper.timeToMillis).toInt())
        fun fromSeconds(seconds: Int): NotificationTime = NotificationTime(seconds)
    }
}