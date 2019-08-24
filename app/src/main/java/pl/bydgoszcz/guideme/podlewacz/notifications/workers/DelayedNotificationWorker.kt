package pl.bydgoszcz.guideme.podlewacz.notifications.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import pl.bydgoszcz.guideme.podlewacz.notifications.NotificationScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DelayedNotificationWorker @Inject constructor(context: Context,
                                                    private val params: WorkerParameters) : Worker(context, params) {
    private val notificationScheduler = NotificationScheduler(context)
    private val tag = "DelayedWorker"

    override fun doWork(): Result {
        val id = params.inputData.getString(NotificationScheduler.ID) ?: return Result.failure()
        val repeat = params.inputData.getLong(NotificationScheduler.REPEAT, 0L)

        try {
            notificationScheduler.startPeriodicNotificationWorker(params.inputData, repeat, id)
            return Result.success()
        } catch (ex: Exception) {
            Log.d(tag, ex.toString())
        }

        return Result.failure()
    }
}