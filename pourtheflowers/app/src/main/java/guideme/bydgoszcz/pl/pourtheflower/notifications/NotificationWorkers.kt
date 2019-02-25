package guideme.bydgoszcz.pl.pourtheflower.notifications

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import guideme.bydgoszcz.pl.pourtheflower.notifications.NotificationPresenter.showNotification

class DelayedNotificationWorker(private val context: Context,
                                private val params: WorkerParameters,
                                private val notificationScheduler: NotificationScheduler) : Worker(context, params) {
    override fun doWork(): Result {
        val id = params.inputData.getString(NotificationScheduler.ID) ?: return Result.FAILURE
        val repeat = params.inputData.getLong(NotificationScheduler.REPEAT, 0L)

        try {
            notificationScheduler.startPeriodicNotificationWorker(params.inputData, repeat, id)
        } catch (ex: Exception) {
            Log.d("DelayedNotWorker", ex.toString())
        }

        return Result.SUCCESS
    }
}

class PeriodicNotificationWorker(private val context: Context, private val params: WorkerParameters)
    : Worker(context, params) {

    override fun doWork(): Result {
        try {
            showNotification(context, params)
        } catch (ex: Exception) {
            Log.d("PeriodicNotWorker", ex.toString())
        }
        return Result.SUCCESS
    }
}
