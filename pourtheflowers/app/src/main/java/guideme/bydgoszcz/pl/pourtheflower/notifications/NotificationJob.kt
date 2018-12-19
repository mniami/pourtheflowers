package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import guideme.bydgoszcz.pl.pourtheflower.MainActivity
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.utils.getColorFromResource

class NotificationJobCreator : JobCreator {
    override fun create(tag: String): Job? {
        when (tag) {
            NotificationJob.TAG ->
                return NotificationJob()
        }
        return null
    }
}

class NotificationJob : Job() {
    companion object {
        const val CHANNEL_ID = "podlewacz"
        const val ID = "notification_id"
        const val TAG = "notification_job"
        const val TITLE = "notification_title"
        const val TEXT = "notification_text"
        const val REPEAT = "notification_repeat"

        fun scheduleJob(id: String, title: String, text: String, delay: Long, repeat: Long) {
            val persistableBundle = PersistableBundleCompat().apply {
                putString(NotificationJob.ID, id)
                putString(NotificationJob.TITLE, title)
                putString(NotificationJob.TEXT, text)
                putLong(NotificationJob.REPEAT, repeat)
            }

            JobRequest.Builder(NotificationJob.TAG)
                    .setExact(delay)
                    .setExtras(persistableBundle)
                    .build()
                    .schedule()
        }
    }

    private var inProgress = false

    override fun onRunJob(params: Params): Result {
        if (inProgress) {
            return Result.FAILURE
        }
        inProgress = true
        try {
            showNotification(params)
        } catch (e: Exception) {
            inProgress = false
        }
        return Result.SUCCESS
    }

    private fun showNotification(params: Params) {
        val id = params.extras.getString(NotificationJob.ID, "")
        val title = params.extras.getString(NotificationJob.TITLE, "")
        val text = params.extras.getString(NotificationJob.TEXT, "")
        val repeat = params.extras.getLong(NotificationJob.REPEAT, 0L)
        val notificationId = id.sumBy { it.toInt() }

        if (notificationId == 0) {
            return
        }
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = NotificationCompat.Builder(context, NotificationJob.CHANNEL_ID)
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
        NotificationJob.scheduleJob(id, title, text, repeat, repeat)
    }
}
