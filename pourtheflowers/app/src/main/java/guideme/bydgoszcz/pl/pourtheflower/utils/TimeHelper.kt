package guideme.bydgoszcz.pl.pourtheflower.utils

internal object TimeHelper {
    const val millisInSecond = 1000
    const val secondsInMinute = 60
    const val minutesInHour = 60
    const val hoursInDay = 24
    const val daysInYear = 356
    const val millisInDay = millisInSecond * secondsInMinute * minutesInHour * hoursInDay
    const val millisInYear = millisInDay * daysInYear
    const val secondsInDay = secondsInMinute * minutesInHour * hoursInDay
    const val secondsInHour = secondsInMinute * minutesInHour
}

