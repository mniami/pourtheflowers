package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import androidx.work.*
import guideme.bydgoszcz.pl.pourtheflower.MainActivity
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource
import java.util.concurrent.TimeUnit

class NotificationWorker(private val context: Context, private val params: WorkerParameters)
    : Worker(context, params) {
    companion object {
        const val MAIN_TAG = "podlewacz"
        const val CHANNEL_ID = "podlewacz"
        const val ID = "notification_id"
        const val TITLE = "notification_title"
        const val TEXT = "notification_text"
        const val REPEAT = "notification_repeat"
        const val DELAY = "notification_delay"

        fun isScheduled(tag : String): Boolean = WorkManager.getInstance().getStatusesByTag(tag).get().size > 0

        fun scheduleJob(id: String, title: String, text: String, delay: Long, repeat: Long) {
            val params = mapOf(
                    NotificationWorker.ID to id,
                    NotificationWorker.TITLE to title,
                    NotificationWorker.TEXT to text,
                    NotificationWorker.REPEAT to repeat,
                    NotificationWorker.DELAY to delay
            )
            val inputData = Data.Builder().putAll(params).build()
            //if (delay == repeat) {
            WorkManager.getInstance().cancelAllWorkByTag(id)

            val notificationWorker = PeriodicWorkRequestBuilder<NotificationWorker>(repeat, TimeUnit.MILLISECONDS)
                    .addTag(id)
                    .addTag(MAIN_TAG)
                    .setInputData(inputData)
                    .build()
            WorkManager.getInstance().enqueue(notificationWorker)

            //} else {
            //    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            //            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            //            .setInputData(inputData)
            //            .build()
            //    WorkManager.getInstance().enqueue(notificationWorker)
            // }
        }
    }

    //private var inProgress = false

    override fun doWork(): Result {
//        if (inProgress) {
//            return Result.failure()
//        }
//        inProgress = true
        try {
            showNotification(params)
        } catch (e: Exception) {
            //inProgress = false
            return Result.FAILURE
        }
        return Result.SUCCESS
    }

    private fun showNotification(params: WorkerParameters) {
        val id = params.inputData.getString(NotificationWorker.ID)
        val title = params.inputData.getString(NotificationWorker.TITLE) ?: return
        val text = params.inputData.getString(NotificationWorker.TEXT) ?: return
        val repeat = params.inputData.getLong(NotificationWorker.REPEAT, 0L)
        val delay = params.inputData.getLong(NotificationWorker.DELAY, 0L)
        //val notificationId = id?.sumBy { it.toInt() } ?: return
        val notificationId = System.currentTimeMillis().toInt()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = NotificationCompat.Builder(context, NotificationWorker.CHANNEL_ID)
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

//        if (delay < repeat) {
//            NotificationWorker.scheduleJob(id, title, text, repeat, repeat)
//        }
    }
}
