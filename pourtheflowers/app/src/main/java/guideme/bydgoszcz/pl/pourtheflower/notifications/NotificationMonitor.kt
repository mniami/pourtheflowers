package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat
import guideme.bydgoszcz.pl.pourtheflower.MainActivity
import guideme.bydgoszcz.pl.pourtheflower.R


class NotificationMonitor(private val activity: FragmentActivity) {
    private val CHANNEL_ID = "podlewacz"
    fun createNotificationChannel(): NotificationMonitor {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Podlewacz"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return this
    }

    fun showNotification(id: String, title: String, text: String, delay: Int) {
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_garden_center_15)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
        val notificationId = id.sumBy { it.toInt() }
        scheduleNotification(notificationId, notificationBuilder.build(), delay)
    }

    private fun scheduleNotification(id: Int, notification: Notification, delay: Int) {
        val notificationIntent = Intent(activity, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }
}