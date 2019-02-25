package guideme.bydgoszcz.pl.pourtheflower.notifications

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationScheduler @Inject constructor() {
    companion object {
        private const val MAIN_TAG = "podlewacz"

        internal const val REPEAT = "notification_repeat"
        internal const val DELAY = "notification_delay"
        internal const val ID = "notification_id"
        internal const val CHANNEL_ID = "podlewacz"
        internal const val TITLE = "notification_title"
        internal const val TEXT = "notification_text"
    }

    fun scheduleJob(id: String, title: String, text: String, delay: Long, repeat: Long) {
        val params = mapOf(
                NotificationScheduler.ID to id,
                NotificationScheduler.TITLE to title,
                NotificationScheduler.TEXT to text,
                NotificationScheduler.REPEAT to repeat,
                NotificationScheduler.DELAY to delay
        )
        val inputData = Data.Builder().putAll(params).build()
        WorkManager.getInstance().cancelAllWorkByTag(id)
        startDelayedNotificationWorker(inputData, delay, id)
    }

    internal fun startPeriodicNotificationWorker(inputData: Data, repeat: Long, id: String) {
        val notificationWorker = PeriodicWorkRequestBuilder<PeriodicNotificationWorker>(repeat, TimeUnit.MILLISECONDS)
                .addTag(id)
                .addTag(MAIN_TAG)
                .setInputData(inputData)
                .build()
        WorkManager.getInstance().enqueue(notificationWorker)
    }

    private fun startDelayedNotificationWorker(inputData: Data, delay: Long, id: String) {
        val notificationWorker = OneTimeWorkRequestBuilder<DelayedNotificationWorker>()
                .addTag(id)
                .addTag(MAIN_TAG)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .build()
        WorkManager.getInstance().enqueue(notificationWorker)
    }
}