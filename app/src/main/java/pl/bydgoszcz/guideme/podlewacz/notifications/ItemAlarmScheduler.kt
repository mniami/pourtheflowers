package pl.bydgoszcz.guideme.podlewacz.notifications

import android.util.Log
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime.Companion.now
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemAlarmScheduler @Inject constructor(private val alarmScheduler: AlarmScheduler) {
    fun schedule() {
        val delay = calculateDelay()
        Log.d(AlarmScheduler.TAG, "Schedule notification with delay: $delay")
        alarmScheduler.schedule(
                delay.toMillis(),
                NotificationTime.fromDays(1).toMillis())
    }

    private fun calculateDelay() : NotificationTime {
        val now = now()
        val todayAlarm = cleanNotificationTimeToFixedTime(now())
        if (todayAlarm > now) {
            return todayAlarm - now
        }
        val tomorrowAlarm = todayAlarm.plus(NotificationTime.fromDays(1))
        return tomorrowAlarm - now
    }
}