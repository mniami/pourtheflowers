package pl.bydgoszcz.guideme.podlewacz.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import javax.inject.Inject

const val ALARM_REQUEST_CODE = 1223
const val ALARM_DATA_EXTRA_INTENT = "AlarmData"

class AlarmScheduler @Inject constructor(val context: Context) {

    private var pendingIntent: PendingIntent? = null

    companion object {
        const val TAG = "AlarmSched"

        internal const val ID = "notification_id"
        internal const val CHANNEL_ID = "podlewacz"
        internal const val TITLE = "notification_title"
    }

    fun schedule(delay: Long, repeat: Long) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val delayScheduledTime = SystemTime.now().plus(NotificationTime.fromMillis(delay)).millis
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = ALARM_DATA_EXTRA_INTENT

        pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, delayScheduledTime, repeat, pendingIntent)
    }
}