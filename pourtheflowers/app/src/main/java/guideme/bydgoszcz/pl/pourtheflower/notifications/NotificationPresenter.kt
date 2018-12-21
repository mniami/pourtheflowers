package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import androidx.work.WorkerParameters
import guideme.bydgoszcz.pl.pourtheflower.MainActivity
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource

object NotificationPresenter {
    fun showNotification(context: Context, params: WorkerParameters) {
        val title = params.inputData.getString(NotificationScheduler.TITLE) ?: return
        val text = params.inputData.getString(NotificationScheduler.TEXT) ?: return
        val notificationId = System.currentTimeMillis().toInt()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = NotificationCompat.Builder(context, NotificationScheduler.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_garden_center_15)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColorized(true)
                .setColor(context.resources.getColorFromResource(R.color.colorPrimary))
                .setAutoCancel(false)
                .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)

        ringtone.play()
        notificationManager.notify(notificationId, notification)
    }
}