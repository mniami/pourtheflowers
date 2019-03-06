package pl.bydgoszcz.guideme.podlewacz.notifications.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import pl.bydgoszcz.guideme.podlewacz.notifications.NotificationPresenter.showNotification

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
