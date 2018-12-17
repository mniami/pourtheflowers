package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager


class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
//        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
//        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
//
//        ringtone.play()
//        notificationManager.notify(id, notification)
    }

    companion object {

        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"
    }
}