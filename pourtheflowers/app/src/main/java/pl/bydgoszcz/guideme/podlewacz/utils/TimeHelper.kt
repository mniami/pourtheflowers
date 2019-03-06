package pl.bydgoszcz.guideme.podlewacz.utils

internal object TimeHelper {
    const val millisInSecond = 1000
    const val secondsInHour = 60
    const val minutesInHour = 60
    const val hoursInDay = 24
    const val millisInDay = millisInSecond * secondsInHour * minutesInHour * hoursInDay
    const val secondsInDay = secondsInHour * minutesInHour * hoursInDay
}

