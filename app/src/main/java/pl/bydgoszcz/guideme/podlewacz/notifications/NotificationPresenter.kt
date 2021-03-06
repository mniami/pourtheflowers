package pl.bydgoszcz.guideme.podlewacz.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import pl.bydgoszcz.guideme.podlewacz.MainActivity
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.utils.getColorFromResource

object NotificationPresenter {
    private const val notificationId = 19222

    fun showNotification(context: Context, title: String, text: String, id: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, ALARM_REQUEST_CODE, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(context, AlarmScheduler.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColorized(true)
                .setColor(context.resources.getColorFromResource(R.color.colorPrimary))
                .setAutoCancel(false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_water_can)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_garden_center_15)
        }
        val notification = notificationBuilder.build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)

        ringtone.play()
        notificationManager.notify(id, notificationId, notification)
    }

    fun removeNotification(context: Context, id: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id, notificationId)
    }
}