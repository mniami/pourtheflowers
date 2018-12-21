package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import guideme.bydgoszcz.pl.pourtheflower.notifications.NotificationPresenter.showNotification

class DelayedNotificationWorker(context: Context,
                                private val repeat: Long,
                                private val id: String,
                                private val params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationScheduler.startPeriodicNotificationWorker(params.inputData, repeat, id)
        return Result.SUCCESS
    }
}

class PeriodicNotificationWorker(private val context: Context, private val params: WorkerParameters)
    : Worker(context, params) {
    override fun doWork(): Result {
        try {
            showNotification(context, params)
        } catch (e: Exception) {
            return Result.FAILURE
        }
        return Result.SUCCESS
    }
}
