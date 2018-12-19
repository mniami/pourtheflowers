package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.evernote.android.job.JobManager
import guideme.bydgoszcz.pl.pourtheflower.actions.SaveUserChanges
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.NotificationJob.Companion.CHANNEL_ID
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime

class ItemsNotifications(private val saveUserChanges: SaveUserChanges) {
    fun setUpNotifications(items: List<UiItem>) {
        items.filter {
            it.item.notification.enabled
        }.forEach {
            val currentTime = SystemTime()
            val delay = it.item.notification.getRemainingTime(currentTime)
            if (it.item.notification.lastNotificationTime.value == 0L) {
                it.item.notification.lastNotificationTime = SystemTime() - SystemTime.seconds(1)
            }
            saveUserChanges.save {
                if (JobManager.instance().allJobs.size > 0){
                    return@save
                }
                NotificationJob.scheduleJob(
                        it.item.id,
                        it.item.name,
                        "Nadszedł czas podlania",
                        delay.toMillis(),
                        it.item.notification.repeatInTime.toMillis())
            }
        }
    }
}
object NotificationChannelManager {
    fun createNotificationChannel(activity : AppCompatActivity) {
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
    }
}
