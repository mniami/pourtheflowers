package guideme.bydgoszcz.pl.pourtheflower.utils

internal object TimeHelper {
    const val millisInSecond = 1000 // milliseconds in second
    const val millisInDay = millisInSecond * 60 * 60 * 24 // millis * seconds * minutes * hours = day expressed in milliseconds
    const val secondsInDay = millisInDay / millisInSecond / 3600
}

