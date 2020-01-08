package pl.bydgoszcz.guideme.podlewacz.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pl.bydgoszcz.guideme.podlewacz.loaders.DataLoader
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceBootReceiver @Inject constructor() : BroadcastReceiver() {
    @Inject
    lateinit var itemsNotifications: ItemsNotifications
    @Inject
    lateinit var dataLoader: DataLoader
    @Inject
    lateinit var repo: ItemsRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            dataLoader.load {
                itemsNotifications.setUpNotifications(repo.user.items)
            }
        }
    }
}