package pl.bydgoszcz.guideme.podlewacz.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import pl.bydgoszcz.guideme.podlewacz.PourTheFlowerApplication
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.loaders.DataLoader
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProvider
import javax.inject.Inject

const val WAKELOCK_TIMEOUT = 10L

class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var contentProvider: ContentProvider
    @Inject
    lateinit var dataLoader: DataLoader
    @Inject
    lateinit var repo: ItemsRepository

    override fun onReceive(context: Context, intent: Intent) { // For our recurring task, we'll just display a message
        (context.applicationContext as PourTheFlowerApplication).component.inject(this)
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (intent.action != ALARM_DATA_EXTRA_INTENT) {
            return
        }
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, NotificationScheduler.TAG)

        wl.acquire(WAKELOCK_TIMEOUT)
        try {
            showNotification(context)
        } finally {
            wl.release()
        }
    }

    private fun showNotification(context: Context) {
        dataLoader.load {
            val notificationTitle = contentProvider.getString(R.string.notification_title)
            repo.user.items.filter {
                Log.d(NotificationScheduler.TAG, "Analyzing '${it.item.name}'")
                it.item.notification.getRemainingSystemTime().isToday()
            }.forEach {
                NotificationPresenter.showNotification(context,
                        title = notificationTitle,
                        text = it.item.name,
                        id = it.item.id
                )
            }
        }
    }
}